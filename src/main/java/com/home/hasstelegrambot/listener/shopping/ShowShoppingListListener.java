package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.service.MessageService;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

@Component
public class ShowShoppingListListener extends AbstractCommandListener {
    public static final String COMMAND = "/shopping_list";

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private MessageService messageService;

    @Override
    protected boolean isSupportCommand(String command) {
        return COMMAND.equals(command);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {
        MessagePayload payload = new MessagePayload();
        payload.setUserList(Collections.singletonList(user));
        List<List<InlineKeyboardKey>> shoppingListKeyboard = shoppingListService.getShoppingListKeyboard();
        payload.setInlineKeyboard(shoppingListKeyboard);

        if (shoppingListKeyboard.size() > 0) {
            payload.setMessage("Список покупок: ");
        } else {
            payload.setMessage("Список покупок пуст");
        }

        messageService.sendMessage(payload);
    }
}
