package com.home.hasstelegrambot.listener.shopping;

import com.google.common.base.Function;
import com.home.hasstelegrambot.config.TelegramBotProperties;
import com.home.hasstelegrambot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractUpdateListener {

    @Autowired
    private TelegramBotProperties telegramProperties;

    @Autowired
    protected MessageService messageService;

    @EventListener
    public void handleTelegramUpdate(Update update) {
        List<Function<Update, Boolean>> parseStrategyList = Arrays.asList(
                this::parseCallback,
                this::parseMessage
        );

        for (Function<Update, Boolean> parseFunction : parseStrategyList) {
            if (parseFunction.apply(update)) {
                break;
            }
        }
    }

    private boolean parseCallback(Update update) {
        Optional<CallbackQuery> callbackContainer = Optional.of(update)
                .map(Update::getCallbackQuery);

        if (callbackContainer.isEmpty()) {
            return false;
        }

        CallbackQuery callbackQuery = callbackContainer.get();

        String user = getUser(update);
        String data = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if (handleCallback(update, user, data, messageId)) {
            messageService.completeCallbackQuery(callbackQuery.getId());
        }

        return true;
    }

    private boolean parseMessage(Update update) {
        Optional<Message> messageContainer = Optional.of(update)
                .map(Update::getMessage);

        if (messageContainer.isEmpty()) {
            return false;
        }

        Message message = messageContainer.get();

        this.handleMessage(update, getUser(update), message);

        return true;
    }

    protected boolean handleCallback(Update update, String user, String data, Integer messageId) {
        return false;
    }

    protected void handleMessage(Update update, String user, Message message) {
        Optional.of(message)
                .map(Message::getText)
                .ifPresent(text -> this.handleText(update, user, message, text));
    }

    protected void handleText(Update update, String user, Message message, String text) {

    }

    private String getUser(Update update) {
        String userId = String.valueOf(AbilityUtils.getUser(update).getId());
        return telegramProperties.getBotChats().inverseBidiMap().get(userId);
    }
}
