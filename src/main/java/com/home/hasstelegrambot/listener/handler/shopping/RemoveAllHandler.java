package com.home.hasstelegrambot.listener.handler.shopping;

import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveAllHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/remove_all";

    @Autowired
    private ShoppingListService shoppingListService;

    public RemoveAllHandler() {
        super(COMMAND, "Очистить список покупок полностью");
        hideCommand = true;
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        shoppingListService.removeAll();

        messageService.notifyAll("Список покупок был полностью очищен");
    }
}
