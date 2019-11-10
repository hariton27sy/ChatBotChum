package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.DataBases.IDataStorage;
import com.ChatBot.DataBases.JSONDataStorage;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.IUserInterface;
import com.ChatBot.Interfaces.TelegramInterface;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.IOException;

public class Chefot {
    private static final IDataStorage dataStorage = new JSONDataStorage();

    public static void main(String[] args){
        IUserInterface bot = new ConsoleInterface();
        if (args.length > 0 && !args[0].equals("-c")){
            if (args[0].equals("-t")) {
                try {
                    ApiContextInitializer.init();
                    bot = new TelegramInterface("token.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        bot.initialize(new BotLogic(dataStorage));
        try {
            bot.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
