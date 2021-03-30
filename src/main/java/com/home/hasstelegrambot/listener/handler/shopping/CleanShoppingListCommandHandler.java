package com.home.hasstelegrambot.listener.handler.shopping;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class CleanShoppingListCommandHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/clean_shopping_list";

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private RemoveCheckedHandler removeCheckedListener;

    public CleanShoppingListCommandHandler() {
        super(COMMAND, "Очистить список покупок");
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        if (shoppingListService.getCountActiveItems() == 0) {
            removeCheckedListener.run();
            return;
        }

        sendResponseWithKeyboard("Что именно удалить?", Collections.singletonList(
                Arrays.asList(
                        new InlineKeyboardKey("✅ Отмеченные", RemoveCheckedHandler.COMMAND),
                        new InlineKeyboardKey("❌ Все", RemoveAllHandler.COMMAND)
                )
        ));
    }
}
