package com.klemmy.novelideas.config;

import com.klemmy.novelideas.client3.NovelIdeasClient3;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties({NovelIdeasClientProperties.class})
@ConditionalOnProperty(name = "novel-ideas.url")
public class NovelIdeasService {

  private final NovelIdeasClientProperties novelIdeasClientProperties;
  private final WebClient webClient;
  private final RestClient restClient;


  // This class is a demo way to show howto create a WebClient and a RestClient to use from another Rest service calling back to novel-idea.
  public NovelIdeasService(NovelIdeasClientProperties novelIdeasClientProperties,
                           WebClient.Builder webClientBuilder, WebClientSsl webSsl,
                           RestClient.Builder restClientBuilder , RestClientSsl restSsl) {
    this.novelIdeasClientProperties = novelIdeasClientProperties;

    this.webClient = webClientBuilder
        .baseUrl(novelIdeasClientProperties.url())
        .apply(webSsl.fromBundle("web-server"))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    this.restClient = restClientBuilder
        .baseUrl(novelIdeasClientProperties.url())
        .apply(restSsl.fromBundle("web-server"))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  // WebClient Started in Spring Framework 5, it is an asynchronous Servlet Stack and was an attempt to replacement to RestTemplate
  @Bean("myWebClient")
  NovelIdeasClient3 clientService() {
// Changes:
// Note1: HttpServiceProxyFactory.builder(HttpClientAdapter clientAdapter) was deprecated for will be removed in Spring Framework 6.2
// Note2: WebClientAdapter.forClient(WebClient webClient) was deprecated for will be removed in Spring Framework 6.2
    HttpServiceProxyFactory proxyFactory =
        HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(this.webClient))
            .build();
    return proxyFactory.createClient(NovelIdeasClient3.class);
  }

  // RestClient Started in Spring Framework 6.1, it is a synchronous Servlet Stack and a replacement to RestTemplate
  @Bean("myRestClient")
  NovelIdeasClient3 clientService2() {
// Changes:
// Note1: HttpServiceProxyFactory.builder(HttpClientAdapter clientAdapter) was deprecated for will be removed in Spring Framework 6.2
// Note2: WebClientAdapter.forClient(WebClient webClient) was deprecated for will be removed in Spring Framework 6.2
    HttpServiceProxyFactory proxyFactory =
        HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(this.restClient))
            .build();
    return proxyFactory.createClient(NovelIdeasClient3.class);
  }
}
