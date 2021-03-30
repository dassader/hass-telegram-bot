package com.home.hasstelegrambot.listener.handler.webhook;

import com.home.hasstelegrambot.listener.handler.AbstractTextUpdateHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.WebhookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class WebhookHandler extends AbstractTextUpdateHandler {
    public static final String WEBHOOK_COMMAND_PREFIX = "/webhook_";

    @Autowired
    private WebhookService webhookService;

    @Override
    public boolean isSupport(String text) {
        return text.startsWith(WEBHOOK_COMMAND_PREFIX);
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        String webhookId = wrapper.getTextForce().replace(WEBHOOK_COMMAND_PREFIX, "");

        log.info("User: {}, call webhook with id: {}", getUser(), webhookId);

        webhookService.callWebhook(webhookId);
    }
}
