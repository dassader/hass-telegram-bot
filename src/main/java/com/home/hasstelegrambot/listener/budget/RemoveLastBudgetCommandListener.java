package com.home.hasstelegrambot.listener.budget;

import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.AbstractCommandListener;
import com.home.hasstelegrambot.repository.NumberStatisticRepository;
import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.home.hasstelegrambot.repository.entity.NumberStatisticType.DAY_BUDGET;

@Component
public class RemoveLastBudgetCommandListener extends AbstractCommandListener {
    public static final String COMMAND = "/remove_last_budget";

    @Autowired
    private NumberStatisticRepository repository;

    @Override
    protected boolean isSupportCommand(String command) {
        return COMMAND.equals(command);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {
        Page<NumberStatisticEntity> response = repository.findAllByType(DAY_BUDGET, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "date")));

        if (response.isEmpty()) {
            messageService.sendMessage(new MessagePayload(user, "Нечего удалять"));
            return;
        }

        NumberStatisticEntity statisticEntity = response.iterator().next();
        repository.delete(statisticEntity);

        MessagePayload payload = new MessagePayload(user, "Удалено");
        messageService.sendMessage(payload);
    }
}
