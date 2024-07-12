package com.klemmy.novelideas.config;

import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {
  private final RestTemplate restTemplate;

  public RestConfiguration(RestTemplateBuilder restTemplateBuilder, SslBundles sslBundles) {
    this.restTemplate = restTemplateBuilder.setSslBundle(sslBundles.getBundle("web-server")).build();
  }

  @Bean(name = "ssl")
  public RestTemplate restTemplateSsl() {
    return this.restTemplate;
  }

}
