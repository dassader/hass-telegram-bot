package com.home.hasstelegrambot.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetHistoryDto {
    private String lastChangeDade;
    private int lastChange;
}
