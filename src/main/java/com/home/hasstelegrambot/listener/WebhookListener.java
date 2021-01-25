package com.home.hasstelegrambot.listener;

import com.home.hasstelegrambot.service.WebhookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
@Component
public class WebhookListener extends AbstractCommandListener {
    public static final String WEBHOOK_COMMAND_PREFIX = "/webhook_";

    @Autowired
    private WebhookService webhookService;

    @Override
    protected boolean isSupportCommand(String command) {
        return command.startsWith(WEBHOOK_COMMAND_PREFIX);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {
        String webhookId = command.replace(WEBHOOK_COMMAND_PREFIX, "");

        log.info("User: {}, call webhook with id: {}", user, webhookId);

        webhookService.callWebhook(webhookId);
    }
}
