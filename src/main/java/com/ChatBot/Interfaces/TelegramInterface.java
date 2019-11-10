package com.ChatBot.Interfaces;

import com.ChatBot.Core.IBotLogic;
import com.ChatBot.Core.Message;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static SendMessage makeKeyboardMessage(long chatId){

        InlineKeyboardButton addButton = new InlineKeyboardButton();
        InlineKeyboardButton showButton = new InlineKeyboardButton();
        InlineKeyboardButton clearButton = new InlineKeyboardButton();
        InlineKeyboardButton showIngredientsButton = new InlineKeyboardButton();
        InlineKeyboardButton removeIngredientButton = new InlineKeyboardButton();

        addButton.setText("Добавить ингредиент");
        addButton.setCallbackData(":Choose ingredient:");
        showButton.setText("Покажи рецепты");
        showButton.setCallbackData(":Show recipes:");
        clearButton.setText("Очисти запрос");
        clearButton.setCallbackData(":Clear request:");
        showIngredientsButton.setText("Покажи доступные ингредиенты");
        showIngredientsButton.setCallbackData(":Show ingredients:");
        removeIngredientButton.setText("Удалить ингредиент");
        removeIngredientButton.setCallbackData(":Choose ingredient:");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>();


        keyboardButtonsRow1.add(addButton);
        keyboardButtonsRow2.add(showButton);
        keyboardButtonsRow3.add(clearButton);
        keyboardButtonsRow4.add(showIngredientsButton);
        keyboardButtonsRow5.add(removeIngredientButton);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        rowList.add(keyboardButtonsRow5);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup().setKeyboard(rowList);
        return new SendMessage()
                .setChatId(chatId)
                .setText("Вам доступны следующие действия:")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public void start() throws TelegramApiRequestException {
        var telegram = new TelegramBotsApi();
        telegram.registerBot(this);
    }

    @Override
    public void stop() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            handleUserMessage(update);
        }
        else if(update.hasCallbackQuery()){
            handleCallbackQuery(update);
        }
    }

    private void handleUserMessage(Update update){
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
            execute(snd);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(Update update){
        var data = update.getCallbackQuery().getData();
        switch (data) {
            case ":Add ingredient:":
                
                break;
            case ":Choose ingredient:":

                break;
            case ":Show recipes:":

                break;
            case ":Clear request:":

                break;
            case ":Show ingredients:":

                break;
            case ":Remove ingredient:":

                break;
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
