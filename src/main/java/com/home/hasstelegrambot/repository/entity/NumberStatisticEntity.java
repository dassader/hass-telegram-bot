package com.home.hasstelegrambot.repository.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@Accessors(chain = true)
public class NumberStatisticEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private OffsetDateTime date;
    private double value;
    @Enumerated(EnumType.STRING)
    private NumberStatisticType type;

    public NumberStatisticEntity(OffsetDateTime date, double value, NumberStatisticType type) {
        this.date = date;
        this.value = value;
        this.type = type;
    }

    public NumberStatisticEntity() {
    }
}
