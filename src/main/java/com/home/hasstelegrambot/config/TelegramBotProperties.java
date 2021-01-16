package com.home.hasstelegrambot.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TelegramBotProperties {
    private String login;
    private String token;
    private List<TelegramUsersProperties> users = new ArrayList<>();
}
