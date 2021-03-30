package com.home.hasstelegrambot.listener.handler.shopping;

import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveCheckedHandler extends AbstractCommandHandler implements Runnable {
    public static final String COMMAND = "/remove_checked";

    @Autowired
    private ShoppingListService shoppingListService;

    public RemoveCheckedHandler() {
        super(COMMAND, "Удалить отмеченные");
        this.hideCommand = true;
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        this.run();
    }

    @Override
    public void run() {
        shoppingListService.removeChecked();

        messageService.notifyAll("Из списка покупок были удалены отмеченные пункты");
    }
}
