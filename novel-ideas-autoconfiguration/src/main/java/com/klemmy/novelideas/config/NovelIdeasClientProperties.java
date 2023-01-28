package com.klemmy.novelideas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "novel-ideas")
public record NovelIdeasClientProperties(String url) {
}
