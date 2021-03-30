package com.home.hasstelegrambot.listener.handler.config;

import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import org.springframework.stereotype.Component;

@Component
public class SetConfigCommandHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/set_config";

    public SetConfigCommandHandler() {
        super(COMMAND, "Сохранить настройки");
    }

    @Override
    public void handle(UpdateWrapper wrapper) {

    }
}
