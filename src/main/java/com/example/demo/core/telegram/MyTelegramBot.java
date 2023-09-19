package com.example.demo.core.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.StringTokenizer;


public class MyTelegramBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(MyTelegramBot.class);

    private final String username;

    public MyTelegramBot(String username, String botToken) {
        super(botToken);
        this.username = username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            LOG.info("update without message");
            return;
        }
        Message message = update.getMessage();
        if (!message.hasText()) {
            LOG.info("message without text");
            return;
        }
        String text = message.getText();

        Command command;
        try {
            command = toCommand(text);
        } catch (RuntimeException ex) {
            LOG.error("unable to convert text to command");
            return;
        }

        Long chatId = message.getChatId();
        switch (command.getName()) {
            case "/echo" -> executeEcho(chatId, command.getBody());
            case "/nextMeeting" -> executeNextMeeting(chatId);
            default -> unknownCommand(chatId, command.getName());
        }
    }

    private void executeEcho(Long chatId, String body) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (body.isBlank()) {
            sendMessage.setText("[EMPTY BODY]");
        } else {
            sendMessage.setText(body);
        }
        sendAnswer(sendMessage);
    }

    private void executeNextMeeting(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("""
                Следующая пара в 10:00 в 1м корпусе в кабинете 339
                Адрес: Театральная площадь
                """);
        sendAnswer(sendMessage);
    }

    private void unknownCommand(Long chatId, String name) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Unknown command [" + name + "]");
        sendAnswer(sendMessage);
    }

    private void sendAnswer(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.info("error during telegram api call", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    private Command toCommand(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        if (!tokenizer.hasMoreTokens()) {
            throw new RuntimeException("empty command");
        }
        String name = tokenizer.nextToken();

        StringBuilder sb = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            sb.append(tokenizer.nextToken());

            if (tokenizer.hasMoreElements()) {
                sb.append(" ");
            }
        }

        String body = sb.toString();

        return new Command(name, body);
    }

    private static class Command {
        private final String name;
        private final String body;

        public Command(String name, String body) {
            this.name = name;
            this.body = body;
        }

        public String getName() {
            return name;
        }

        public String getBody() {
            return body;
        }
    }

}
