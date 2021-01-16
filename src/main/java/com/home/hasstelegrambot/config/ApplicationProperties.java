package com.home.hasstelegrambot.config;

import lombok.Data;

@Data
public class ApplicationProperties {
    private int port;
    private TelegramBotProperties telegram;
}
