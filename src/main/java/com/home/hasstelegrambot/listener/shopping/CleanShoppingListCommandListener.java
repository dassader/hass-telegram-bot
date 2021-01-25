package com.home.hasstelegrambot.listener.shopping;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.AbstractCommandListener;
import com.home.hasstelegrambot.service.MessageService;
import com.home.hasstelegrambot.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Component
public class CleanShoppingListCommandListener extends AbstractCommandListener {
    public static final String COMMAND = "/clean_shopping_list";

    @Autowired
    private MessageService messageService;

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private RemoveCheckedListener removeCheckedListener;

    @Override
    protected boolean isSupportCommand(String command) {
        return COMMAND.equals(command);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {

        if (shoppingListService.getCountActiveItems() == 0) {
            removeCheckedListener.handleCommand(update, user, command);
            return;
        }

        MessagePayload payload = new MessagePayload();
        payload.setMessage("Что именно удалить?");
        payload.setUserList(Arrays.asList(user));


        payload.setInlineKeyboard(Arrays.asList(
                Arrays.asList(new InlineKeyboardKey("✅ Отмеченные", RemoveCheckedListener.COMMAND), new InlineKeyboardKey("❌ Все", RemoveAllListener.COMMAND))
        ));

        messageService.sendMessage(payload);
    }
}
