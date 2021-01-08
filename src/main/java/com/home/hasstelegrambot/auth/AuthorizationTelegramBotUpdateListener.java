package com.home.hasstelegrambot.auth;

import com.home.hasstelegrambot.bot.TelegramBotUpdateEvent;
import com.home.hasstelegrambot.config.TelegramBotProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collection;

@Log4j2
@Component
public class AuthorizationTelegramBotUpdateListener {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private TelegramBotProperties telegramBotProperties;

    @EventListener
    public void handleContextStart(TelegramBotUpdateEvent telegramBotUpdateEvent) {
        Update update = telegramBotUpdateEvent.getUpdate();

        Long chatId = AbilityUtils.getChatId(update);

        Collection<String> listChats = telegramBotProperties.getBotChats().values();

        if (listChats.contains(String.valueOf(chatId))) {
            eventPublisher.publishEvent(update);
        } else {
            User user = AbilityUtils.getUser(update);
            log.warn("Unauthorized message from {}", user.getUserName());
        }
    }
}
