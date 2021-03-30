package com.home.hasstelegrambot.listener.handler;

import lombok.Getter;

@Getter
public abstract class AbstractCommandHandler extends AbstractTextUpdateHandler {
    private String command;
    private String description;
    protected boolean hideCommand = false;

    public AbstractCommandHandler(String command, String description) {
        this.command = command;
        this.description = description;
    }

    @Override
    public boolean isSupport(String text) {
        return command.equals(text);
    }
}
