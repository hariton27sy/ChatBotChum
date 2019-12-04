package com.ChatBot.Interfaces;

import com.ChatBot.Core.IBotLogic;
import com.ChatBot.Core.Message;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TelegramInterface extends TelegramLongPollingBot implements IUserInterface {
    private IBotLogic botLogic;
    private String token;
    private HashMap<String, UserActs> usersCurrentAction;

    public TelegramInterface(String tokenFile) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(tokenFile));
        token = file.readLine();
    }

    @Override
    public void initialize(IBotLogic botLogic) {
        this.botLogic = botLogic;
        usersCurrentAction = new HashMap<>();
    }

    private static SendMessage makeKeyboardMessage(long chatId){
        InlineKeyboardButton addButton = new InlineKeyboardButton();
        InlineKeyboardButton showButton = new InlineKeyboardButton();
        InlineKeyboardButton clearButton = new InlineKeyboardButton();
        InlineKeyboardButton removeIngredientButton = new InlineKeyboardButton();

        addButton.setText("Добавь ингредиент");
        addButton.setCallbackData(":Choose ingredient to add:");
        showButton.setText("Покажи рецепт");
        showButton.setCallbackData(":Show recipe:");
        clearButton.setText("Очисти запрос");
        clearButton.setCallbackData(":Clear request:");
        removeIngredientButton.setText("Удали ингредиент");
        removeIngredientButton.setCallbackData(":Choose ingredient to remove:");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();


        keyboardButtonsRow1.add(addButton);
        keyboardButtonsRow2.add(showButton);
        keyboardButtonsRow3.add(clearButton);
        keyboardButtonsRow4.add(removeIngredientButton);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup().setKeyboard(rowList);
        return new SendMessage()
                .setChatId(chatId)
                .setText("Тебе доступны следующие действия:")
                .setReplyMarkup(keyboard);
    }

    private static SendMessage makeKeyboardMessageFrom(Collection<String> collection, String query){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(String elem : collection){
            rowList.add(new ArrayList<>() {{
                add(new InlineKeyboardButton().setText(elem).setCallbackData(query + elem));
            }});
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup().setKeyboard(rowList);
        return new SendMessage()
                .setText("Выберите ингредиент для удаления")
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
        SendMessage snd;
        String userName = null;
        Long chatId = null;
        if(update.hasCallbackQuery()){
            userName = update.getCallbackQuery().getMessage().getChat().getUserName();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            snd = handleCallbackQuery(update, userName, chatId);
        }
        else if(update.hasMessage()){
            userName = update.getMessage().getChat().getUserName();
            chatId = update.getMessage().getChatId();
            snd = handleUserMessage(update, userName, chatId);
        }
        else{
            snd = new SendMessage().setText("");
        }

        try{
            execute(snd);
            if(shouldSendKeyboardMessage(userName, chatId))
                execute(makeKeyboardMessage(chatId));
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    private SendMessage handleUserMessage(Update update, String userName, Long chatId){
        var msg = update.getMessage();
        SendMessage snd = new SendMessage();
        if(usersCurrentAction.get(userName) == UserActs.Adding)
            snd.setText(getBotAnswer(userName, "добавь " + msg.getText()));

        else
            snd.setText(getBotAnswer(userName, msg.getText()));
        usersCurrentAction.put(userName, UserActs.ChoosingAction);
        snd.setChatId(chatId);
        return snd;
    }

    private SendMessage handleCallbackQuery(Update update, String userName, Long chatId){
        String data = update.getCallbackQuery().getData();
        var snd = new SendMessage();
        if (":Choose ingredient to add:".equals(data)){
            snd.setText("Введи название ингредиента:");
            usersCurrentAction.put(userName, UserActs.Adding);

        } else if (":Choose ingredient to remove:".equals(data)) {
            snd = makeKeyboardMessageFrom(botLogic.getAddedIngredients(userName), ":Remove ingredient:-");
            usersCurrentAction.put(userName, UserActs.Deleting);

        } else if (":Show recipe:".equals(data)) {
            snd.setText(getBotAnswer(userName, "покажи"));
            usersCurrentAction.put(userName, UserActs.ChoosingAction);

        } else if (":Clear request:".equals(data)) {
            snd.setText(getBotAnswer(userName, "очисти"));
            usersCurrentAction.put(userName, UserActs.ChoosingAction);

        } else if (data.contains(":Remove ingredient:")) {
            String[] splittedData = data.split("-");
            if(splittedData.length > 1){
                String ingredientName = data.split("-")[1];
                snd.setText(getBotAnswer(userName, String.format("удали %s", ingredientName)));
            }
            else
                snd.setText("Тебе нечего удалять! Обманщик!");
            usersCurrentAction.put(userName, UserActs.ChoosingAction);
        }

        snd.setChatId(chatId);
        return snd;
    }

    private Boolean shouldSendKeyboardMessage(String userName, Long chatId){
        return userName != null && chatId != null &&
                usersCurrentAction.get(userName) == UserActs.ChoosingAction;
    }

    private String getBotAnswer(String userName, String message){
        try {
            return botLogic.analyzeAndGetAnswer(userName,
                    new Message(message));
        } catch (Exception e) {
            return "Что-то пошло не так. :(";
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
