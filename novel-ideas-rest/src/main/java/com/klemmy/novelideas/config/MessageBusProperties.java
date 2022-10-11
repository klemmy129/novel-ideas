package com.klemmy.novelideas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "message-bus")
public record MessageBusProperties(
    String type,
    String brokerUrl,
    String topic
) {
}
