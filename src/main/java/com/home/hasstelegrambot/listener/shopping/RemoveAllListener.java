package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.listener.AbstractCommandListener;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RemoveAllListener extends AbstractCommandListener {
    public static final String COMMAND = "/remove_all";

    @Autowired
    private ShoppingListService shoppingListService;

    @Override
    protected boolean isSupportCommand(String command) {
        return COMMAND.equals(command);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {
        shoppingListService.removeAll();

        messageService.notifyAll("Список покупок был полностью очищен");
    }
}
