package com.home.hasstelegrambot.repository.entity.config;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@Accessors(chain = true)
public class BudgetConfigEntity extends ConfigEntity {
    private int salaryDay;
    private int chartWidth;
    private int chartHeight;
    private int chartDefaultMaxValue;
    private int chartDefaultMinValue;
    @Column(columnDefinition = "integer default 900")
    private int budgetLimit;

    public BudgetConfigEntity() {
        this.setType(ConfigType.BUDGET);
    }
}
