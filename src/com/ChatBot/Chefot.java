package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.Message;
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
        //TODO: Multi-user authentication
        userPool.add(authenticateUser());
        bot.send(String.format("Привет, %s!", userPool.get(userPool.size() - 1).username));
        while(true){
            for(int i = 0; i < userPool.size(); i++){
                String userInput = bot.receive();
                String answer = BotLogic.analyzeAndGetAnswer(
                        userPool.get(i),
                        new Message(userInput));
                bot.send(answer);
            }
        }
    }

    private static UserInfo authenticateUser(){
        bot.send("Введите ваше имя:");
        return new UserInfo(bot.receive());
    }
}
