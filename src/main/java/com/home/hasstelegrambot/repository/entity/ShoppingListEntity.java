package com.home.hasstelegrambot.repository.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Accessors(chain = true)
public class ShoppingListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String text;

    private boolean checked;

    public ShoppingListEntity(String text) {
        this.text = text;
        this.checked = false;
    }

    public ShoppingListEntity() {

    }
}
