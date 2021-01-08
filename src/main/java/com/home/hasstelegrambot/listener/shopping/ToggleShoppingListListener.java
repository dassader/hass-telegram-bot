package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

@Component
public class ToggleShoppingListListener extends AbstractUpdateListener {

    @Autowired
    private ShoppingListService shoppingListService;

    @Override
    protected boolean handleCallback(Update update, String user, String data, Integer messageId) {
        if(!data.startsWith(ShoppingListService.TOGGLE_PREFIX)) {
            return false;
        }

        shoppingListService.handleToggleCommand(data);

        MessagePayload payload = new MessagePayload();
        payload.setUserList(Collections.singletonList(user));
        payload.setMessage("Список покупок: ");
        payload.setInlineKeyboard(shoppingListService.getShoppingListKeyboard());

        messageService.editMessage(messageId, payload);

        return true;
    }
}
