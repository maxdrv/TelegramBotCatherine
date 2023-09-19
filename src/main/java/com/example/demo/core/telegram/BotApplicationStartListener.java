package com.example.demo.core.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotApplicationStartListener {

    private static final Logger LOG = LoggerFactory.getLogger(BotApplicationStartListener.class);

    private final TelegramBotsApi telegramBotsApi;
    private final MyTelegramBot myTelegramBot;

    public BotApplicationStartListener(TelegramBotsApi telegramBotsApi, MyTelegramBot myTelegramBot) {
        this.telegramBotsApi = telegramBotsApi;
        this.myTelegramBot = myTelegramBot;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registerBotOnApplicationStart() throws TelegramApiException {
        LOG.info("Register bot...");
        telegramBotsApi.registerBot(myTelegramBot);
    }

}
