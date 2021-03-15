package com.home.hasstelegrambot.listener.budget;

import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.AbstractUpdateListener;
import com.home.hasstelegrambot.repository.NumberStatisticRepository;
import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import com.home.hasstelegrambot.service.ChartService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.home.hasstelegrambot.repository.entity.NumberStatisticType.DAY_BUDGET;

@Log4j2
@Component
public class CalculateDayTillSalary extends AbstractUpdateListener {

    public static final int SALARY_DAY = 5;
    public static final String RESPONSE_TEMPLATE = "Сегодня %s \n%d дня до %s \n%d/%d = %d";

    @Autowired
    private NumberStatisticRepository statisticRepository;

    @Autowired
    private ChartService chartService;

    @Override
    protected void handleText(Update update, String user, Message message, String text) {
        if(!NumberUtils.isDigits(text)) {
            return;
        }

        String response = generateResponseMessage(text);
        MessagePayload payload = new MessagePayload(user, response);

        Page<NumberStatisticEntity> statisticEntities =
                statisticRepository.findAllByType(DAY_BUDGET, PageRequest.of(0, 45, Sort.by(Sort.Direction.ASC, "date")));

        chartService.generateNumberStatisticChart(statisticEntities, "История", "Бюджет")
                .ifPresent(payload::addImage);

        messageService.sendImages(payload);
    }

    private String generateResponseMessage(String text) {
        OffsetDateTime now = OffsetDateTime.now();

        int bill = Integer.parseInt(text);
        int dayTillSalary = getDaysCountTillSalary(now);
        String targetDate;
        if(dayTillSalary > SALARY_DAY) {
            targetDate = now.plusMonths(1).withDayOfMonth(SALARY_DAY).format(DateTimeFormatter.ofPattern("dd.MM"));
        } else {
            targetDate = now.withDayOfMonth(SALARY_DAY).format(DateTimeFormatter.ofPattern("dd.MM"));
        }
        String currentDate = now.format(DateTimeFormatter.ofPattern("dd.MM"));
        int dayBudget = bill / dayTillSalary;

        statisticRepository.save(new NumberStatisticEntity(now, dayBudget, DAY_BUDGET));

        return String.format(RESPONSE_TEMPLATE, currentDate, dayTillSalary, targetDate, bill, dayTillSalary, dayBudget);
    }

    protected int getDaysCountTillSalary(OffsetDateTime now) {
        int count = 1;

        for (OffsetDateTime date = now; date.getDayOfMonth() != SALARY_DAY; date = date.plusDays(1)) {
            count++;
        }

        return count;
    }
}
