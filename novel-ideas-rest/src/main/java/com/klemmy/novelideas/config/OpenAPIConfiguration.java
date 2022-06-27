package com.klemmy.novelideas.config;

import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration extends SwaggerConfiguration {

  @Bean
  public OpenAPI springOpenAPI(
      @Value("${info.app.version}") String appVersion,
      @Value("${server.servlet.context-path}") String contextPath
  ) {
    return new OpenAPI()
        .addServersItem(new Server().url(contextPath))
        .info(new Info()
            .title("Novel Ideas")
            .description("This Rest backend is for managing data and information about a novel or book you want to write")
            .version(appVersion)
            .contact(new Contact()
                .email("a.klemm@hotmail.com")
                .name("Klemmy129"))
            .license(new License()
                .name("GNU GPL-3.0 License"))
        );
  }
}
