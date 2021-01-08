package com.home.hasstelegrambot.listener.shopping;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractCommandListener extends AbstractUpdateListener {

    protected abstract boolean isSupportCommand(String command);

    protected abstract void handleCommand(Update update, String user, String command);

    @Override
    protected boolean handleCallback(Update update, String user, String data, Integer messageId) {
        if (!this.isSupportCommand(data)) {
            return false;
        }

        handleCommand(update, user, data);

        return true;
    }

    @Override
    protected void handleText(Update update, String user, Message message, String text) {
        if(!this.isSupportCommand(text)) {
            return;
        }

        handleCommand(update, user, text);
    }
}
