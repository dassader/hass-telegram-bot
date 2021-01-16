package com.home.hasstelegrambot.auth;

import com.home.hasstelegrambot.bot.TelegramBotUpdateEvent;
import com.home.hasstelegrambot.config.TelegramBotProperties;
import com.home.hasstelegrambot.config.TelegramUsersProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class AuthorizationTelegramBotUpdateListener {

    private ApplicationEventPublisher eventPublisher;
    private List<String> userList = new ArrayList<>();

    public AuthorizationTelegramBotUpdateListener(ApplicationEventPublisher eventPublisher, TelegramBotProperties telegramBotProperties) {
        this.eventPublisher = eventPublisher;

        for (TelegramUsersProperties user : telegramBotProperties.getUsers()) {
            userList.add(user.getChatId());
        }
    }

    @EventListener
    public void handleContextStart(TelegramBotUpdateEvent telegramBotUpdateEvent) {
        Update update = telegramBotUpdateEvent.getUpdate();

        Long chatId = AbilityUtils.getChatId(update);

        if (userList.contains(String.valueOf(chatId))) {
            eventPublisher.publishEvent(update);
        } else {
            User user = AbilityUtils.getUser(update);
            log.warn("Unauthorized message from {} with chat id: {}", user.getUserName(), user.getId());
        }
    }
}
