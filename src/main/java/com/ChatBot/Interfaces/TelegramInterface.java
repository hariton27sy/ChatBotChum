package com.ChatBot.Interfaces;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.Message;
import com.ChatBot.Core.UserInfo;
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

public class TelegramInterface{
    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        var telegram = new TelegramBotsApi();
        telegram.registerBot(new TelegramBot());
    }
}

class TelegramBot extends TelegramLongPollingBot{
    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var snd = new SendMessage();
        try {
            snd.setText(BotLogic.analyzeAndGetAnswer(new UserInfo(msg.getAuthorSignature()),
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
        try {
            return readToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readToken() throws IOException {
        var file = new BufferedReader(new FileReader("token.txt"));
        return file.readLine();
    }
}

