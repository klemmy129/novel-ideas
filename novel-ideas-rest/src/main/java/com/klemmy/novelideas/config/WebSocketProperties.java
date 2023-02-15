package com.klemmy.novelideas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stomp")
public record WebSocketProperties(
    String url,
    Integer port,
    String account,
    String passcode
) {
}
