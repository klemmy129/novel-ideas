package com.klemmy.novelideas.config;

import com.klemmy.novelideas.controller.websocket.NotifyController;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
//@ConditionalOnProperty(name = "stomp", havingValue = "url")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final WebSocketProperties webSocketProperties;

  public WebSocketConfig(WebSocketProperties webSocketProperties) {
    this.webSocketProperties = webSocketProperties;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/").setAllowedOrigins("/ws");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/zapi");

//    Use without Message bus
//  registry.enableSimpleBroker("/all", "/specific");

//    Use with Message bus
//    registry.enableStompBrokerRelay("/all", "/specific")
//        .setRelayHost("127.0.0.1")
//        .setRelayPort(61613)
//        .setClientLogin("guest")
//        .setClientPasscode("guest");
//  }
    registry.enableStompBrokerRelay("/all", "/specific")
        .setRelayHost(this.webSocketProperties.url())
        .setRelayPort(this.webSocketProperties.port())
        .setClientLogin(this.webSocketProperties.account())
        .setClientPasscode(this.webSocketProperties.passcode());
  }

}
