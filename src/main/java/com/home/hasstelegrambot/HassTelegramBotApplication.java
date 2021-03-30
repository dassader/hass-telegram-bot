package com.home.hasstelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HassTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HassTelegramBotApplication.class, args);
    }

}
