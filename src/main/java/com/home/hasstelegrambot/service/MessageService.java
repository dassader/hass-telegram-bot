package com.home.hasstelegrambot.service;

import com.home.hasstelegrambot.config.CustomApplicationProperties;
import com.home.hasstelegrambot.config.TelegramUserListProperties;
import com.home.hasstelegrambot.controller.dto.InlineKeyboardKey;
import com.home.hasstelegrambot.controller.dto.MessagePayload;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MessageService {

    @Autowired
    private TelegramLongPollingBot telegramBot;

    @Autowired
    private CustomApplicationProperties properties;

    public void sendImages(MessagePayload payload) {
        List<File> imageFiles = replaceFolderToFilePath(payload.getImageList());

        if(imageFiles.isEmpty()) {
            return;
        }

        if(imageFiles.size() == 1) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setCaption(payload.getMessage());
            sendPhoto.setPhoto(new InputFile(imageFiles.get(0)));

            for (String user : payload.getUserList()) {
                sendPhoto.setChatId(getChatId(user));
                try {
                    telegramBot.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    log.error("Error while send single photo", e);
                }
            }
        } else {
            List<InputMedia> inputMedia = new ArrayList<>();
            for (File imageFile : imageFiles) {
                InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                inputMediaPhoto.setMedia(imageFile, UUID.randomUUID().toString());
                inputMedia.add(inputMediaPhoto);
            }

            SendMediaGroup group = new SendMediaGroup();
            group.setMedias(inputMedia);

            for (String user : payload.getUserList()) {
                group.setChatId(getChatId(user));
                try {
                    telegramBot.execute(group);
                } catch (TelegramApiException e) {
                    log.error("Error while send message with photos", e);
                }
            }
        }
    }

    private List<File> replaceFolderToFilePath(List<String> imagePathList) {
        List<File> result = new ArrayList<>();

        for (String path : imagePathList) {
            File file = new File(path);
            if (file.isDirectory()) {
                List<File> files = Arrays.stream(file.listFiles())
                        .filter(File::isFile)
                        .collect(Collectors.toList());
                result.addAll(files);
            } else {
                result.add(file);
            }
        }

        return result;
    }

    public void sendMessage(MessagePayload payload) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(payload.getMessage());

        if(payload.getInlineKeyboard().size() > 0) {
            InlineKeyboardMarkup keyboard = buildInlineKeyboard(payload.getInlineKeyboard());
            sendMessage.setReplyMarkup(keyboard);
        }

        for (String user : payload.getUserList()) {
            try {
                sendMessage.setChatId(getChatId(user));
                telegramBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Error while send message", e);
            }
        }
    }

    public void editMessage(Integer messageId, MessagePayload payload) {
        EditMessageText.EditMessageTextBuilder builder = EditMessageText.builder()
                .chatId(getChatId(payload.getUserList().get(0)))
                .messageId(messageId)
                .text(payload.getMessage());

        if(payload.getInlineKeyboard().size() > 0) {
            InlineKeyboardMarkup keyboard = buildInlineKeyboard(payload.getInlineKeyboard());
            builder.replyMarkup(keyboard);
        }

        try {
            telegramBot.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Error while edit message", e);
        }
    }

    private InlineKeyboardMarkup buildInlineKeyboard(List<List<InlineKeyboardKey>> inlineKeyboard) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();

        for (List<InlineKeyboardKey> rowBlueprint : inlineKeyboard) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            for (InlineKeyboardKey buttonBlueprint : rowBlueprint) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonBlueprint.getText());
                button.setCallbackData(buttonBlueprint.getData());
                keyboardRow.add(button);
            }
            builder.keyboardRow(keyboardRow);
        }

        return builder.build();
    }

    public void completeCallbackQuery(String callbackQueryId) {
        try {
            telegramBot.execute(new AnswerCallbackQuery(callbackQueryId));
        } catch (TelegramApiException e) {
            log.error("Error while complete callback query");
        }
    }

    private String getChatId(String user) {
        for (TelegramUserListProperties userProperties : properties.getUsers()) {
            if (user.equals(userProperties.getUsername())) {
                return userProperties.getChatId();
            }
        }

        return null;
    }

    public void notifyAll(String message) {
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setMessage(message);

        List<String> userIdList = properties.getUsers()
                .stream().map(TelegramUserListProperties::getUsername)
                .collect(Collectors.toList());

        messagePayload.setUserList(userIdList);
        sendMessage(messagePayload);
    }

    public void deleteMessage(String user, Integer messageId) {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(getChatId(user))
                .messageId(messageId)
                .build();

        try {
            telegramBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Error while delete message", e);
        }
    }
}
