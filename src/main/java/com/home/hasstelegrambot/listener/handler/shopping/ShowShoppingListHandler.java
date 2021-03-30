package com.home.hasstelegrambot.listener.handler.shopping;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowShoppingListHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/shopping_list";

    @Autowired
    private ShoppingListService shoppingListService;

    public ShowShoppingListHandler() {
        super(COMMAND, "Показать список покупок");
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        List<List<InlineKeyboardKey>> shoppingListKeyboard = shoppingListService.getShoppingListKeyboard();

        if (shoppingListKeyboard.size() > 0) {
            sendResponseWithKeyboard("Список покупок: ", shoppingListKeyboard);
        } else {
            sendResponse("Список покупок пуст");
        }
    }
}
