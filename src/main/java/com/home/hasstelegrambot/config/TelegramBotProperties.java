package com.home.hasstelegrambot.config;

import lombok.Data;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Data
@Configuration
@ConfigurationProperties(prefix = "telegram")
@PropertySources({
        @PropertySource(value = "file:./data/options.json", factory = YamlPropertySourceFactory.class)
})
public class TelegramBotProperties {
    private String botLogin;
    private String botToken;
    private BidiMap<String, String> botChats = new DualHashBidiMap<>();
}
