package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.service.MessageService;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AddItemToShoppingListListener extends AbstractUpdateListener {

    public static final String SHOPPING_LIST_ITEM_PREFIX = "-";

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private MessageService messageService;

    @Override
    protected void handleText(Update update, String user, Message message, String text) {
        if(!text.startsWith(SHOPPING_LIST_ITEM_PREFIX)) {
            return;
        }

        List<String> items = shoppingListService.addItems(text);

        StringBuilder response = new StringBuilder("Товары добавлены в список покупок: \n");

        for (int i = 0; i < items.size(); i++) {
            response.append(i+1).append(". ").append(items.get(i)).append("\n");
        }

        messageService.notifyAll(response.toString());
    }
}
