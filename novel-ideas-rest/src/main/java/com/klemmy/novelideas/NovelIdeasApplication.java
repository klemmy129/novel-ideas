package com.klemmy.novelideas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.klemmy.novelideas.jpa")
public class NovelIdeasApplication {

  public static void main(String[] args) {
    SpringApplication.run(NovelIdeasApplication.class, args);
  }

}
