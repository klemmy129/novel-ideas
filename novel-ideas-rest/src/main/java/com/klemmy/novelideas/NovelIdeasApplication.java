package com.klemmy.novelideas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Novel or script structure",
        description = "This Rest backend is for managing data and information about a novel or book you want to write",
        version = "1.0", contact = @Contact(email = "a.klemm@hotmail.com", name = "Anthony Klemm")))
@EnableJpaRepositories(basePackages = "com.klemmy.novelideas.jpa")
public class NovelIdeasApplication {

  public static void main(String[] args) {
    SpringApplication.run(NovelIdeasApplication.class, args);
  }

}