package com.home.hasstelegrambot.listener.handler;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.TelegramBotUpdateContext;
import com.home.hasstelegrambot.listener.TelegramBotUpdateContextProvider;
import com.home.hasstelegrambot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public abstract class AbstractResponseHandler implements UpdateHandler {
    @Autowired
    protected MessageService messageService;

    @Autowired
    protected TelegramBotUpdateContextProvider contextProvider;

    private void sendResponse(String message, List<String> imageList, List<List<InlineKeyboardKey>> inlineKeyboard) {
        MessagePayload messagePayload = new MessagePayload(getUser(), message);

        if (imageList != null) {
            messagePayload.setImageList(imageList);
            messageService.sendImages(messagePayload);
            return;
        }

        if (inlineKeyboard != null) {
            messagePayload.setInlineKeyboard(inlineKeyboard);
        }

        messageService.sendMessage(messagePayload);
    }

    protected void sendResponse(String message) {
        sendResponse(message, null, null);
    }

    protected void sendResponseWithImage(String message, String image) {
        sendResponse(message, Collections.singletonList(image), null);
    }

    protected void sendResponseWithKeyboard(String message, List<List<InlineKeyboardKey>> inlineKeyboard) {
        sendResponse(message, null, inlineKeyboard);
    }

    protected String getUser() {
        TelegramBotUpdateContext context = contextProvider.get();
        return context.getUsername();
    }
}
