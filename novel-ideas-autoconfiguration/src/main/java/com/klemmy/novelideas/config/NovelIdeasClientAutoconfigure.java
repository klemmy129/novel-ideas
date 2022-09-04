package com.klemmy.novelideas.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({com.klemmy.novelideas.config.NovelIdeasClientConfiguration.class, com.klemmy.novelideas.config.RestConfiguration.class})
public class NovelIdeasClientAutoconfigure {

}
