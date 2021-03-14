package com.home.hasstelegrambot.listener.budget;

import com.home.hasstelegrambot.controller.dto.MessagePayload;
import com.home.hasstelegrambot.listener.AbstractUpdateListener;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Component
public class CalculateDayTillSalary extends AbstractUpdateListener {

    public static final int SALARY_DAY = 5;
    public static final String RESPONSE_TEMPLATE = "Сегодня %s \n%d дня до %s \n%d/%d = %d";

    @Override
    protected void handleText(Update update, String user, Message message, String text) {
        if(!NumberUtils.isDigits(text)) {
            return;
        }

        int bill = Integer.parseInt(text);
        OffsetDateTime now = OffsetDateTime.now();
        int dayTillSalary = getDaysCountTillSalary(now);

        String targetDate;
        if(dayTillSalary > SALARY_DAY) {
            targetDate = now.plusMonths(1).withDayOfMonth(SALARY_DAY).format(DateTimeFormatter.ofPattern("dd.MM"));
        } else {
            targetDate = now.withDayOfMonth(SALARY_DAY).format(DateTimeFormatter.ofPattern("dd.MM"));
        }

        String currentDate = now.format(DateTimeFormatter.ofPattern("dd.MM"));

        String response = String.format(RESPONSE_TEMPLATE, currentDate, dayTillSalary, targetDate, bill, dayTillSalary, bill / dayTillSalary);

        MessagePayload payload = new MessagePayload();
        payload.setUserList(Collections.singletonList(user));
        payload.setMessage(response);

        messageService.sendMessage(payload);
    }

    protected int getDaysCountTillSalary(OffsetDateTime now) {
        int count = 1;

        for (OffsetDateTime date = now; date.getDayOfMonth() != SALARY_DAY; date = date.plusDays(1)) {
            count++;
        }

        return count;
    }
}
