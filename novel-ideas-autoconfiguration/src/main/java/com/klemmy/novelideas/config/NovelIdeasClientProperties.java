package com.klemmy.novelideas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "novel-ideas")
public record NovelIdeasClientProperties(String url) {
}
