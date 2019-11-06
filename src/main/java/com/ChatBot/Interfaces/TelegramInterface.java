package com.ChatBot.Interfaces;

import com.ChatBot.Core.IBotLogic;
import com.ChatBot.Core.Message;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TelegramInterface extends TelegramLongPollingBot implements IUserInterface {
    private IBotLogic botLogic;
    private String token;

    public TelegramInterface(String tokenFile) throws IOException {
        var file = new BufferedReader(new FileReader(tokenFile));
        token = file.readLine();
    }

    @Override
    public void initialize(IBotLogic botLogic) {
        this.botLogic = botLogic;
    }

    @Override
    public void start() throws TelegramApiRequestException {
        var telegram = new TelegramBotsApi();
        telegram.registerBot(this);
        //Add Я - шефот, и могу помочь тебе выбрать блюдо на вечер. Или на утро. Или перекус.\n" +
        //                "В общем, не стесняйся, говори, что ты хочешь, а я подскажу ;)\n" +
        //                "Для того, чтобы узнать, как со мной работать, напиши '/help'.
    }

    @Override
    public void stop() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var snd = new SendMessage();
        try {
            snd.setText(botLogic.analyzeAndGetAnswer(msg.getChat().getUserName(),
                    new Message(msg.getText())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        snd.setChatId(msg.getChatId());
        try {
            sendMessage(snd);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Chefot";
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
