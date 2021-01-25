package com.home.hasstelegrambot.service;

import com.home.hasstelegrambot.config.CustomApplicationProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Log4j2
@Service
public class WebhookService {

    @Autowired
    private CustomApplicationProperties applicationProperties;

    @Autowired
    private Supplier<HttpClient> clientSupplier;

    public void callWebhook(String webhookId) {
        String webhookUrl = applicationProperties.getWebhookUrl() + "/" + webhookId;
        HttpPost httpPost = new HttpPost(webhookUrl);
        HttpClient httpClient = clientSupplier.get();

        try {
            httpClient.execute(httpPost);
        } catch (Exception e) {
            log.error("Error while call webhook url", e);
        }
    }
}
