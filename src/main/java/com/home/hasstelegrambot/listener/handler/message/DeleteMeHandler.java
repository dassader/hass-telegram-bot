package com.home.hasstelegrambot.listener.handler.message;

import com.home.hasstelegrambot.listener.TelegramBotUpdateContextProvider;
import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteMeHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/delete_me";

    @Autowired
    private TelegramBotUpdateContextProvider contextProvider;

    public DeleteMeHandler() {
        super(COMMAND, "Удалить сообщение");
        hideCommand = true;
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        Integer messageId = wrapper.getMessageIdForce();
        String chatId = contextProvider.get().getChatId();

        messageService.deleteMessage(chatId, messageId);
    }
}
