package com.home.hasstelegrambot.listener.handler.budget;

import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.repository.NumberStatisticRepository;
import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.home.hasstelegrambot.repository.entity.NumberStatisticType.DAY_BUDGET;

@Component
public class RemoveLastBudgetCommandHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/remove_last_budget";

    @Autowired
    private NumberStatisticRepository repository;

    public RemoveLastBudgetCommandHandler() {
        super(COMMAND, "Удалить последние изменение бюджета");
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        Page<NumberStatisticEntity> response = repository.findAllByType(DAY_BUDGET, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "date")));

        if (response.isEmpty()) {
            sendResponse("Нечего удалять");
            return;
        }

        NumberStatisticEntity statisticEntity = response.iterator().next();
        repository.delete(statisticEntity);

        sendResponse("Удалено");
    }
}
