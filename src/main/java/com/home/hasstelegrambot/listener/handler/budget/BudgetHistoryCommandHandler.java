package com.home.hasstelegrambot.listener.handler.budget;

import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.BudgetService;
import com.home.hasstelegrambot.service.dto.BudgetHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.home.hasstelegrambot.service.BudgetService.CHART_FILE_PATH;

@Component
public class BudgetHistoryCommandHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/budget_history";
    public static final String RESPONSE_TEMPLATE = "Дата последнего изменения:    %s\nБюджет:%40s%d";

    @Autowired
    private BudgetService budgetService;


    public BudgetHistoryCommandHandler() {
        super(COMMAND, "История изменений бюджета");
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        Optional<BudgetHistoryDto> response = budgetService.getBudgetHistory();

        if (!response.isPresent()) {
            sendResponse("Изменений бюджета не найдено. Попробуйте отправить сообщение содержащие только цифры.");
            return;
        }

        BudgetHistoryDto budgetHistory = response.get();

        String responseMessage = String.format(RESPONSE_TEMPLATE, budgetHistory.getLastChangeDade(), "", budgetHistory.getLastChange());

        sendResponseWithImage(responseMessage, CHART_FILE_PATH);
    }
}
