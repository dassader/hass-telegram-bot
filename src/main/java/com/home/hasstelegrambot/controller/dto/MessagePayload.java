package com.home.hasstelegrambot.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessagePayload {
    private String message;
    private List<String> userList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private List<List<InlineKeyboardKey>> inlineKeyboard = new ArrayList<>();
}
