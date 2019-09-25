package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.MessageParser;
import com.ChatBot.Core.UserInfo;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.InOutInterface;

import java.util.ArrayList;
import java.util.List;

public class Chefot {
    static InOutInterface bot;

    private final static List<UserInfo> userPool = new ArrayList<>();

    public static void main(String[] args) {
        bot = new ConsoleInterface();
        userPool.add(bot.initializeNewUser());
        bot.greetUser(userPool.get(userPool.size() - 1));
        while(true){
            for(int i = 0; i < userPool.size(); i++){
                String userInput = bot.receive();
                String answer = BotLogic.analyzeAndGetAnswer(
                        userPool.get(i),
                        MessageParser.parseMessage(userInput));
                bot.send(answer);
            }
        }
    }
}
