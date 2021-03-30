package com.home.hasstelegrambot.listener.handler.shopping;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.handler.AbstractTextUpdateHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.listener.handler.message.DeleteMeHandler;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class ToggleShoppingListHandler extends AbstractTextUpdateHandler {
    @Autowired
    private ShoppingListService shoppingListService;

    @Override
    public boolean isSupport(String text) {
        return text.startsWith(ShoppingListService.TOGGLE_PREFIX);
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        String data = wrapper.getText().get();

        shoppingListService.handleToggleCommand(data);

        MessagePayload payload = new MessagePayload();
        payload.setUserList(Collections.singletonList(getUser()));
        payload.setMessage("Список покупок: ");
        payload.setInlineKeyboard(shoppingListService.getShoppingListKeyboard());
        messageService.editMessage(wrapper.getMessageIdForce(), payload);

        if (shoppingListService.getCountActiveItems() == 0) {
            MessagePayload cleanQuestion = new MessagePayload();
            cleanQuestion.setUserList(Collections.singletonList(getUser()));
            cleanQuestion.setMessage("Все пункты отмечены. Хотите очистить список?");
            cleanQuestion.setInlineKeyboard(Collections.singletonList(
                    Arrays.asList(
                            new InlineKeyboardKey("Да 👍🏻", CleanShoppingListCommandHandler.COMMAND),
                            new InlineKeyboardKey("Нет 🚫", DeleteMeHandler.COMMAND)
                    )
            ));
            messageService.sendMessage(cleanQuestion);
        }
    }
}
