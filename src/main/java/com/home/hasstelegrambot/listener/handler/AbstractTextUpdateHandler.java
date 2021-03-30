package com.home.hasstelegrambot.listener.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractTextUpdateHandler extends AbstractResponseHandler {
    @Override
    public boolean isSupport(Update update) {
        return new UpdateWrapper(update).getText()
                .map(this::isSupport)
                .orElse(false);
    }

    @Override
    public void handle(Update update) {
        UpdateWrapper updateWrapper = new UpdateWrapper(update);

        try {
            this.handle(updateWrapper);
        } finally {
            updateWrapper.getCallback()
                    .map(CallbackQuery::getId)
                    .ifPresent(messageService::completeCallbackQuery);
        }
    }

    public abstract boolean isSupport(String text);

    public abstract void handle(UpdateWrapper wrapper);
}
