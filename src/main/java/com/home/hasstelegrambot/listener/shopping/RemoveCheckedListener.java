package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RemoveCheckedListener extends AbstractCommandListener {
    public static final String COMMAND = "/remove_checked";

    @Autowired
    private ShoppingListService shoppingListService;

    @Override
    protected boolean isSupportCommand(String command) {
        return COMMAND.equals(command);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {
        shoppingListService.removeChecked();

        messageService.notifyAll("Из списка покупок были удалены отмеченные пункты");
    }
}
