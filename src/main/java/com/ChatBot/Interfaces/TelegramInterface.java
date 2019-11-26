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
import java.util.Collection;
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
//        InlineKeyboardButton addButton = new InlineKeyboardButton();
        InlineKeyboardButton showButton = new InlineKeyboardButton();
        InlineKeyboardButton clearButton = new InlineKeyboardButton();
        InlineKeyboardButton showIngredientsButton = new InlineKeyboardButton();
        InlineKeyboardButton removeIngredientButton = new InlineKeyboardButton();

//        addButton.setText("Добавить ингредиент");
//        addButton.setCallbackData(":Choose ingredient to add:");
        showButton.setText("Покажи рецепты");
        showButton.setCallbackData(":Show recipes:");
        clearButton.setText("Очисти запрос");
        clearButton.setCallbackData(":Clear request:");
        showIngredientsButton.setText("Покажи доступные ингредиенты");
        showIngredientsButton.setCallbackData(":Show ingredients:");
        removeIngredientButton.setText("Удалить ингредиент");
        removeIngredientButton.setCallbackData(":Choose ingredient to remove:");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>();


//        keyboardButtonsRow1.add(addButton);
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
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup().setKeyboard(rowList);
        return new SendMessage()
                .setChatId(chatId)
                .setText("Вам доступны следующие действия:")
                .setReplyMarkup(keyboard);
    }

    private static SendMessage makeKeyboardMessageFrom(Collection<String> collection, String query){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(String elem : collection){
            rowList.add(new ArrayList<>() {{
                add(new InlineKeyboardButton().setText(elem).setCallbackData(query));
            }});
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup().setKeyboard(rowList);
        return new SendMessage()
                .setText("Вам доступны следующие действия:")
                .setReplyMarkup(keyboard);
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
        SendMessage snd = new SendMessage().setText("");
        if(update.hasCallbackQuery()){
            snd = handleCallbackQuery(update);
        }
        else if(update.hasMessage()){
            snd = handleUserMessage(update);
        }
        else{
            snd = null;
        }
        try{
            execute(snd);
            execute(makeKeyboardMessage(update.getMessage().getChatId()));
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    private SendMessage handleUserMessage(Update update){
        var msg = update.getMessage();
        var snd = new SendMessage();
        try {
            snd.setText(botLogic.analyzeAndGetAnswer(msg.getChat().getUserName(),
                    new Message(msg.getText())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        snd.setChatId(msg.getChatId());
        return snd;
    }

    private SendMessage handleCallbackQuery(Update update){
        var data = update.getCallbackQuery().getData();
        var snd = new SendMessage();
        var userName = update.getCallbackQuery().getMessage().getChat().getUserName();
        switch (data) {
//            case ":Add ingredient:":
//                try {
//                    snd.setText(botLogic.analyzeAndGetAnswer(userName,
//                            new Message(String.format("добавь %s", update.getCallbackQuery().getInlineMessageId()))));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case ":Choose ingredient to add:":
//                snd = new SendMessage().setText("Введите название ингредиента")
//                break;
            case ":Choose ingredient to remove:":
                snd = makeKeyboardMessageFrom(botLogic.getAddedIngredients(userName), ":Remove ingredient:");
                break;
            case ":Show recipes:":
                try {
                    snd.setText(botLogic.analyzeAndGetAnswer(userName,
                            new Message("покажи")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ":Clear request:":
                try {
                    snd.setText(botLogic.analyzeAndGetAnswer(userName,
                            new Message("очисти")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ":Show ingredients:":
                try {
                    snd.setText(botLogic.analyzeAndGetAnswer(userName,
                            new Message("ингредиенты")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ":Remove ingredient:":
                try {
                    snd.setText(botLogic.analyzeAndGetAnswer(userName,
                            new Message(String.format("удали %s", update.getCallbackQuery().getInlineMessageId()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        snd.setChatId(update.getCallbackQuery().getMessage().getChatId());
        return snd;
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
