package com.home.hasstelegrambot.listener.budget;

import com.google.common.collect.Iterables;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.AbstractCommandListener;
import com.home.hasstelegrambot.repository.NumberStatisticRepository;
import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import com.home.hasstelegrambot.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.home.hasstelegrambot.repository.entity.NumberStatisticType.DAY_BUDGET;

@Component
public class BudgetHistoryCommandListener extends AbstractCommandListener {
    public static final String COMMAND = "/budget_history";
    public static final String RESPONSE_TEMPLATE = "Дата последнего изменения:    %s\nБюджет:%40s%d";

    @Autowired
    private NumberStatisticRepository statisticRepository;

    @Autowired
    private ChartService chartService;

    @Override
    protected boolean isSupportCommand(String command) {
        return COMMAND.equals(command);
    }

    @Override
    protected void handleCommand(Update update, String user, String command) {
        Page<NumberStatisticEntity> statisticEntities =
                statisticRepository.findAllByType(DAY_BUDGET, PageRequest.of(0, 45, Sort.by(Sort.Direction.ASC, "date")));

        if (statisticEntities.isEmpty()) {
            messageService.sendMessage(new MessagePayload(user, "Изменений бюджета не найдено. Попробуйте отправить сообщение содержащие только цифры."));
            return;
        }

        List<NumberStatisticEntity> statisticList = statisticEntities.getContent();
        NumberStatisticEntity lastChange = Iterables.getLast(statisticList);
        String lastChangeDate = lastChange.getDate().format(DateTimeFormatter.ofPattern("dd.MM"));

        String responseMessage = String.format(RESPONSE_TEMPLATE, lastChangeDate, "", (int) lastChange.getValue());

        MessagePayload messagePayload = new MessagePayload(user, responseMessage);

        chartService.generateNumberStatisticChart(statisticList)
                .ifPresent(messagePayload::addImage);

        messageService.sendImages(messagePayload);
    }
}
