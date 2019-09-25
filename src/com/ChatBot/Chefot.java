package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.MessageParser;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.InOutInterface;

public class Chefot {
    InOutInterface bot;

    private final List<UserInfo> userPool = new List<UserInfo>;

    public void main(String[] args) {
        bot = new ConsoleInterface();
        userPool.add(initializeNewUser());
        while(true){
            foreach(UserInfo user in userPool){
                String userInput = bot.receive();
                String answer = BotLogic.analyzeAndGetAnswer(username, MessageParser.parseMessage(userInput));
                bot.send(answer);
            }
        }
    }

    private UserInfo initializeNewUser(){
        bot.send("Enter your name: ")
        return new UserInfo(bot.receive())
    }
}
