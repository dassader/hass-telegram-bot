package com.home.hasstelegrambot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.hasstelegrambot.bot.TelegramBot;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Log4j2
@Configuration
public class TelegramBotConfig {



    @Bean
    public TelegramBotsApi botsApi(LongPollingBot longPollingBot) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(longPollingBot);
            return telegramBotsApi;
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Error while create telegram bot API", e);
        }
    }

    @Bean
    public TelegramLongPollingBot telegramBot(TelegramBotProperties properties, ApplicationEventPublisher eventPublisher) {
        if (properties.getToken() == null) {
            throw new IllegalArgumentException("Bot token not found");
        }

        return new TelegramBot(properties.getLogin(), properties.getToken(), eventPublisher);
    }

    @Bean
    public TelegramBotProperties telegramBotProperties(ObjectMapper objectMapper, ResourceLoader resourceLoader) throws IOException {
        File file = resourceLoader.getResource("file:./data/configprops.json").getFile();
        return objectMapper.readValue(file, TelegramBotProperties.class);
    }
}
