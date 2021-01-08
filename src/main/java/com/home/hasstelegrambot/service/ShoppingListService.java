package com.home.hasstelegrambot.service;

import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.repository.ShoppingListRepository;
import com.home.hasstelegrambot.repository.entity.ShoppingListEntity;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ShoppingListService {
    public static final String TOGGLE_PREFIX = "/toggle_";

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    public List<String> addItems(String text) {
        List<String> items = new ArrayList<>();

        for (String itemText : text.split("\n")) {
            itemText = itemText.replace("-", "");
            itemText = itemText.trim();

            items.add(itemText);
            shoppingListRepository.save(new ShoppingListEntity(itemText));
        }

        return items;
    }

    public List<List<InlineKeyboardKey>> getShoppingListKeyboard() {
        List<List<InlineKeyboardKey>> shoppingList = new ArrayList<>();

        List<ShoppingListEntity> uncheckedItems = IterableUtils.toList(shoppingListRepository.findAllByCheckedIsFalse());

        for (ShoppingListEntity shoppingListEntity : uncheckedItems) {
            InlineKeyboardKey inlineKeyboardKey = new InlineKeyboardKey(shoppingListEntity.getText(), TOGGLE_PREFIX + shoppingListEntity.getId());
            shoppingList.add(Collections.singletonList(inlineKeyboardKey));
        }

        List<ShoppingListEntity> checkedItems = IterableUtils.toList(shoppingListRepository.findAllByCheckedIsTrue());

        if (checkedItems.size() > 0 && uncheckedItems.size() > 0) {
            shoppingList.add(Collections.singletonList(new InlineKeyboardKey(" ", "space")));
        }

        for (ShoppingListEntity shoppingListEntity : checkedItems) {
            InlineKeyboardKey inlineKeyboardKey = new InlineKeyboardKey("✅ " + shoppingListEntity.getText(), TOGGLE_PREFIX + shoppingListEntity.getId());
            shoppingList.add(Collections.singletonList(inlineKeyboardKey));
        }

        return shoppingList;
    }

    public void handleToggleCommand(String toggleCommand) {
        String itemId = toggleCommand.replace(TOGGLE_PREFIX, "");

        shoppingListRepository.findById(Integer.parseInt(itemId))
                .map(shoppingListEntity -> shoppingListEntity.setChecked(!shoppingListEntity.isChecked()))
                .ifPresent(entity -> shoppingListRepository.save(entity));
    }

    @Transactional
    public void removeChecked() {
        shoppingListRepository.deleteByCheckedIsTrue();
    }

    public void removeAll() {
        shoppingListRepository.deleteAll();
    }
}
