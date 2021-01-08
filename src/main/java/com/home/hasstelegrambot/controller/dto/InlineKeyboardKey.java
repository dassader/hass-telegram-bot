package com.home.hasstelegrambot.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InlineKeyboardKey {
    private String text;
    private String data;
}
