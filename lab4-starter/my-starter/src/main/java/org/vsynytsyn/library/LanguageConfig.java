package org.vsynytsyn.library;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LanguageConfig {

    @Bean
    @ConditionalOnProperty(value = "localization.code", havingValue = "en", matchIfMissing = true)
    Language english() {
        return new EnglishLanguage();
    }


    @Bean
    @ConditionalOnProperty(value = "localization.code", havingValue = "ukr")
    Language ukrainian() {
        return new UkrainianLanguage();
    }


    @Bean
    @ConditionalOnProperty(value = "localization.code", havingValue = "de")
    Language german() {
        return new GermanLanguage();
    }
}
