package com.home.hasstelegrambot.auth;

import com.home.hasstelegrambot.listener.TelegramBotUpdateEvent;
import com.home.hasstelegrambot.config.CustomApplicationProperties;
import com.home.hasstelegrambot.config.TelegramUserListProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class AuthorizationTelegramBotUpdateListener {

    private ApplicationEventPublisher eventPublisher;
    private Map<String, String> userMap = new HashMap<>();


    public AuthorizationTelegramBotUpdateListener(ApplicationEventPublisher eventPublisher, CustomApplicationProperties properties) {
        this.eventPublisher = eventPublisher;

        for (TelegramUserListProperties user : properties.getUsers()) {
            userMap.put(user.getChatId(), user.getUsername());
        }
    }

    @EventListener
    public void handleContextStart(Update update) {
        String chatId = Optional.ofNullable(update)
                .map(AbilityUtils::getChatId)
                .map(String::valueOf)
                .orElse("");

        if (userMap.containsKey(chatId)) {
            TelegramBotUpdateEvent event = new TelegramBotUpdateEvent(update, userMap.get(chatId), chatId);
            eventPublisher.publishEvent(event);
        } else {
            User user = AbilityUtils.getUser(update);
            log.warn("Unauthorized message from {} with chat id: {}", user.getUserName(), user.getId());
        }
    }
}
