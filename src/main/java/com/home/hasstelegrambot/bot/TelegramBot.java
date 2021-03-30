package com.home.hasstelegrambot.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Log4j2
public class TelegramBot extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;
    private ApplicationEventPublisher applicationEventPublisher;

    public TelegramBot(String botUsername, String botToken, ApplicationEventPublisher applicationEventPublisher) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Receive update with id: {}", update.getUpdateId());
        applicationEventPublisher.publishEvent(update);
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {
        log.warn("Call clear webhook");
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        log.info("Bot register complete");
    }
}
