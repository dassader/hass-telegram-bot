package com.home.hasstelegrambot.listener;

import com.home.hasstelegrambot.listener.handler.UpdateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Log4j2
@Component
public class TelegramBotUpdateListener {

    @Autowired
    private TelegramBotUpdateContextProvider context;

    @Autowired
    private List<UpdateHandler> updateHandlerList;

    @Async
    @EventListener
    public void handleTelegramUpdate(TelegramBotUpdateEvent event) {
        Update update = event.getUpdate();

        try {
            context.set(new TelegramBotUpdateContext(event.getUsername(), event.getChatId()));

            for (UpdateHandler updateHandler : updateHandlerList) {
                if (updateHandler.isSupport(update)) {
                    log.info("Handle request by: {}", updateHandler.getClass().getSimpleName());
                    updateHandler.handle(update);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error while handle update", e);
        } finally {
            context.close();
        }
    }
}
