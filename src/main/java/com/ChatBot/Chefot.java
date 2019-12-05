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

        IUserInterface bot = new ConsoleInterface();
        if (args.length > 0 && !args[0].equals("-c")){
            if (args[0].equals("-t")) {
                if (args.length < 2){
                    System.out.println("You need to give path to a file with token");
                    return;
                }
                try {
                    ApiContextInitializer.init();
                    bot = new TelegramInterface(args[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            if (args[0].equals("-sql") || args.length > 2 && args[2].equals("-sql")){
                if (args.length < 2 || args.length > 2 && args[2].equals("-sql") && args.length < 4){
                    System.out.println("You need to give path to a file with sql settings");
                    return;
                }
                try{
                    dataStorage = new MySQLDataBase((args.length < 3) ? args[1] : args[3]);
                } catch (Exception e) {
                    e.printStackTrace();
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
