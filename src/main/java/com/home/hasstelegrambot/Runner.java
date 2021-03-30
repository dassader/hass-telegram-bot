package com.home.hasstelegrambot;

import com.home.hasstelegrambot.listener.handler.AbstractCommandHandler;
import com.home.hasstelegrambot.repository.ConfigRepository;
import com.home.hasstelegrambot.repository.entity.config.BudgetConfigEntity;
import com.home.hasstelegrambot.repository.entity.config.ConfigEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.home.hasstelegrambot.repository.entity.config.ConfigType.BUDGET;

@Log4j2
@Component
@AllArgsConstructor
public class Runner implements CommandLineRunner {

    private ConfigRepository configRepository;

    @Autowired
    private TelegramLongPollingBot telegramBot;

    @Autowired
    private List<AbstractCommandHandler> commandList;

    @Override
    public void run(String... args) throws Exception {
        initDefaultConfig();
        initCommand();
    }

    private void initCommand() throws TelegramApiException {
        List<BotCommand> commandList = this.commandList.stream()
                .filter(command -> !command.isHideCommand())
                .map(command -> new BotCommand(command.getCommand(), command.getDescription()))
                .collect(Collectors.toList());

        telegramBot.execute(new SetMyCommands(commandList));
    }

    private void initDefaultConfig() {
        Optional<ConfigEntity> budgetConfig = configRepository.findById(BUDGET);

        if (!budgetConfig.isPresent()) {
            log.info("Budget config not found. Init default values.");

            BudgetConfigEntity config = new BudgetConfigEntity()
                    .setSalaryDay(5)
                    .setChartWidth(800)
                    .setChartHeight(600)
                    .setChartDefaultMaxValue(2000)
                    .setChartDefaultMinValue(200)
                    .setBudgetLimit(900);

            configRepository.save(config);
        }
    }
}
