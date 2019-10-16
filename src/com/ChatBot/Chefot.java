package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.Message;
import com.ChatBot.Core.UserInfo;
import com.ChatBot.DataBases.JSONDataStorage;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.InOutInterface;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.List;

public class Chefot {
    static InOutInterface bot;

    private final static List<UserInfo> userPool = new ArrayList<>();

    public static void main(String[] args) throws ExecutionControl.NotImplementedException {
        bot = new ConsoleInterface();
        //TODO: Multi-user authentication
        userPool.add(authenticateUser());
        bot.send(String.format("Привет, %s!", userPool.get(userPool.size() - 1).username));
        bot.send("Я - шефот, и могу помочь тебе выбрать блюдо на вечер. Или на утро. Или перекус.\n" +
                "В общем, не стесняйся, говори, что ты хочешь, а я подскажу ;)\n" +
                "Для того, чтобы узнать, как со мной работать, напиши '/help'.");
        while(true){
            for(int i = 0; i < userPool.size(); i++){
                String userInput = bot.receive();
                String answer = BotLogic.analyzeAndGetAnswer(
                        userPool.get(i),
                        new Message(userInput));
                if (answer.equals("Q")) {
                    bot.send("Пока");
                    return;
                }
                bot.send(answer);
            }
        }
    }

    private static UserInfo authenticateUser(){
        bot.send("Введите ваше имя:");
        return new UserInfo(bot.receive());
    }
}
