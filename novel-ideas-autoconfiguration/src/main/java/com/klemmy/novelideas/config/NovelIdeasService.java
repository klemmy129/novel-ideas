package com.klemmy.novelideas.config;

import com.klemmy.novelideas.client3.NovelIdeasClient3;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@EnableConfigurationProperties({NovelIdeasClientProperties.class, SslProperties.class})
@ConditionalOnProperty(name = "novel-ideas.url")
public class NovelIdeasService {

  private final NovelIdeasClientProperties novelIdeasClientProperties;
  private final SslProperties sslProperties;

  public NovelIdeasService(NovelIdeasClientProperties novelIdeasClientProperties, SslProperties sslProperties) {
    this.novelIdeasClientProperties = novelIdeasClientProperties;
    this.sslProperties = sslProperties;
  }

  @Bean
  NovelIdeasClient3 clientService() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
    String trustStorePassword = sslProperties.trustStorePassword();
    KeyStore trustStore = KeyStore.getInstance(sslProperties.trustStoreType());
    trustStore.load(new FileInputStream(sslProperties.trustStore()), trustStorePassword.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    SslContext sslContext = SslContextBuilder
        .forClient()
        .trustManager(tmf)
        .build();
    HttpClient httpClient = HttpClient.create().secure(ssl -> ssl.sslContext(sslContext));
    ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

    WebClient client = WebClient.builder()
        .baseUrl(novelIdeasClientProperties.url())
        .clientConnector(httpConnector)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    HttpServiceProxyFactory proxyFactory =
        HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
            .build();

    return proxyFactory.createClient(NovelIdeasClient3.class);
  }
}
