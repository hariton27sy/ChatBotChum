package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.DataBases.IDataStorage;
import com.ChatBot.DataBases.JSONDataStorage;
import com.ChatBot.DataBases.MySQLDataBase;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.IUserInterface;
import com.ChatBot.Interfaces.TelegramInterface;
import com.google.inject.internal.cglib.core.$Constants;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.IOException;

public class Chefot {
    private static IDataStorage dataStorage = new JSONDataStorage();

    public static void main(String[] args){
        try{
            dataStorage = new MySQLDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (e.getMessage().contains("timed out"))
                System.out.println("Can not to connect to bot");
            else
                e.printStackTrace();
        }
        return;
    }
}
