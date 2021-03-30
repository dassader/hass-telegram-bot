package com.home.hasstelegrambot.listener;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TelegramBotUpdateContext {
    private String username;
    private String chatId;
}
