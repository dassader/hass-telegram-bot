package com.home.hasstelegrambot.controller;

import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    private void sendMessage(@RequestBody MessagePayload messagePayload) {
        if (messagePayload.getImageList().size() > 0) {
            messageService.sendImages(messagePayload);
        } else {
            messageService.sendMessage(messagePayload);
        }
    }
}
