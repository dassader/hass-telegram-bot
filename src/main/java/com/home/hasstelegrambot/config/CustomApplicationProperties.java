package com.home.hasstelegrambot.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomApplicationProperties {
    private int port;
    private String login;
    private String token;
    private List<TelegramUserListProperties> users = new ArrayList<>();
}
