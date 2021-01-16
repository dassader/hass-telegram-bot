package com.home.hasstelegrambot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ServerPortConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Autowired
    private ApplicationProperties properties;

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(properties.getPort());
    }
}
