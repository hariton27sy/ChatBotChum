package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.DataBases.IDataStorage;
import com.ChatBot.DataBases.JSONDataStorage;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.IUserInterface;
import com.ChatBot.Interfaces.TelegramInterface;

public class Chefot {
    private static final IDataStorage dataStorage = new JSONDataStorage();

    public static void main(String[] args) throws Exception {
        IUserInterface bot = new ConsoleInterface();
        if (args.length > 0 && !args[0].equals("-c")){
            if (args[0].equals("-t"))
                bot = new TelegramInterface("token.txt");
        }

        bot.initialize(new BotLogic(dataStorage));
        bot.start();
    }
}
