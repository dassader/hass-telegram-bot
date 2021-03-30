package com.home.hasstelegrambot.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BudgetCalculateDto {
    private String currentDate;
    private int dayTillSalary;
    private String targetDate;
    private int dayBudget;
    private int globalDeviation;
}
