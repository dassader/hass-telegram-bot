package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.AbstractUpdateListener;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
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

        if (shoppingListService.getCountActiveItems() == 0) {
            MessagePayload cleanQuestion = new MessagePayload();
            cleanQuestion.setUserList(Collections.singletonList(user));
            cleanQuestion.setMessage("Все пункты отмечены. Хотите очистить список?");
            cleanQuestion.setInlineKeyboard(Collections.singletonList(
                    Arrays.asList(
                            new InlineKeyboardKey("Да 👍🏻", CleanShoppingListCommandListener.COMMAND),
                            new InlineKeyboardKey("Нет 🚫", DeleteMeListener.COMMAND)
                    )
            ));
            messageService.sendMessage(cleanQuestion);
        }

        return true;
    }
}
