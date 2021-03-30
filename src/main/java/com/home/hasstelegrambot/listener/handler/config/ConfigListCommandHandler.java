package com.home.hasstelegrambot.listener.handler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.listener.handler.UpdateWrapper;
import com.home.hasstelegrambot.repository.ConfigRepository;
import com.home.hasstelegrambot.repository.entity.config.ConfigEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigListCommandHandler extends AbstractCommandHandler {
    public static final String COMMAND = "/config";

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public ConfigListCommandHandler() {
        super(COMMAND, "Настройки в JSON формате");
    }

    @Override
    @SneakyThrows
    public void handle(UpdateWrapper wrapper) {
        List<ConfigEntity> configList = configRepository.findAll();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(configList);

        sendResponse(json);
    }
}
