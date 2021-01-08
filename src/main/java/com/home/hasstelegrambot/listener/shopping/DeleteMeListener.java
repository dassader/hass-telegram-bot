package com.home.hasstelegrambot.listener.shopping;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeleteMeListener extends AbstractUpdateListener {
    public static final String COMMAND = "/delete_me";

    @Override
    protected boolean handleCallback(Update update, String user, String data, Integer messageId) {
        if (!COMMAND.equals(data)) {
            return false;
        }

        messageService.deleteMessage(user, messageId);

        return true;
    }
}
