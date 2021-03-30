package com.home.hasstelegrambot.listener;

import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class TelegramBotUpdateContextProvider implements Supplier<TelegramBotUpdateContext> {

    private ThreadLocal<TelegramBotUpdateContext> threadLocal = new ThreadLocal<>();

    void set(TelegramBotUpdateContext context) {
        threadLocal.set(context);
    }

    void close() {
        threadLocal.remove();
    }

    @Override
    public TelegramBotUpdateContext get() {
        return threadLocal.get();
    }
}
