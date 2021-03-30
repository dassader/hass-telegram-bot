package com.home.hasstelegrambot.listener.handler.budget;

import com.home.hasstelegrambot.listener.handler.AbstractTextUpdateHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.service.BudgetService;
import com.home.hasstelegrambot.service.dto.BudgetCalculateDto;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CalculateDayTillSalaryHandler extends AbstractTextUpdateHandler {
    public static final String RESPONSE_TEMPLATE = "Сегодня %s \n%d дня до %s \n%d/%d = %d\nГлобальное отклонение: %d";

    @Autowired
    private BudgetService budgetService;

    @Override
    public boolean isSupport(String text) {
        return NumberUtils.isDigits(text);
    }

    @Override
    public void handle(UpdateWrapper wrapper) {
        int money = wrapper.getText().map(Integer::parseInt).orElseThrow(() -> new IllegalArgumentException("Update didn't contain text"));

        BudgetCalculateDto budgetDto = budgetService.updateBudget(money);

        String response = String.format(RESPONSE_TEMPLATE, budgetDto.getCurrentDate(),
                budgetDto.getDayTillSalary(), budgetDto.getTargetDate(), money,
                budgetDto.getDayTillSalary(), budgetDto.getDayBudget(), budgetDto.getGlobalDeviation());

        sendResponseWithImage(response, BudgetService.CHART_FILE_PATH);
    }

}
