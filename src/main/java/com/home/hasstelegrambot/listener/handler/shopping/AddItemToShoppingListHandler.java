package com.home.hasstelegrambot.listener.handler.shopping;

import com.home.hasstelegrambot.listener.handler.AbstractTextUpdateHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.MessageService;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddItemToShoppingListHandler extends AbstractTextUpdateHandler {

    public static final String SHOPPING_LIST_ITEM_PREFIX = "-";

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private MessageService messageService;

    @Override
    public boolean isSupport(String text) {
        return text.startsWith(SHOPPING_LIST_ITEM_PREFIX);
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        String text = wrapper.getTextForce();
        List<String> items = shoppingListService.addItems(text);

        StringBuilder response = new StringBuilder("Товары добавлены в список покупок: \n");

        for (int i = 0; i < items.size(); i++) {
            response.append(i+1).append(". ").append(items.get(i)).append("\n");
        }

        messageService.notifyAll(response.toString());
    }
}
