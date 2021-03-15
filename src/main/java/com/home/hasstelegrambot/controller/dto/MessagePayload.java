package com.home.hasstelegrambot.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class MessagePayload {
    private String message;
    private List<String> userList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private List<List<InlineKeyboardKey>> inlineKeyboard = new ArrayList<>();

    public MessagePayload() {
    }

    public MessagePayload(String user, String message) {
        this.userList = Collections.singletonList(user);
        this.message = message;
    }

    public void addImage(String filePath) {
        imageList.add(filePath);
    }
}
