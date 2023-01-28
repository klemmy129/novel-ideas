package com.klemmy.novelideas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server.ssl")
public record SslProperties(
    String keyStore,
    String keyStorePassword,
    String keyStoreType,
    String trustStore,
    String trustStorePassword,
    String trustStoreType) {
}
