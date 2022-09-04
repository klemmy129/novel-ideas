package com.klemmy.novelideas.config;

import com.klemmy.novelideas.client.NovelIdeasClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(NovelIdeasClientProperties.class)
@ConditionalOnProperty(name = "novel-ideas.url")
@DependsOn("ssl")
public class NovelIdeasClientConfiguration {

  private final NovelIdeasClientProperties novelIdeasClientProperties;

  public NovelIdeasClientConfiguration(NovelIdeasClientProperties novelIdeasClientProperties) {
    this.novelIdeasClientProperties = novelIdeasClientProperties;
  }

  @Bean ("NovelIdeaClientConfig")
  public NovelIdeasClient clientConfig(@Qualifier("ssl") RestTemplate restTemplate) {
    return new NovelIdeasClient(novelIdeasClientProperties.url(), restTemplate);
  }

}
