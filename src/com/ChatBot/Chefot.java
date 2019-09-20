package com.ChatBot;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.MessageParser;
import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.InOutInterface;

public class Chefot {
    InOutInterface bot;

    public void main(String[] args) {
        bot = new ConsoleInterface();
        while(true){
            String userInput = bot.receive();
            String username = "admin";
            String answer = BotLogic.analyzeAndGetAnswer(username, MessageParser.parseMessage(userInput));
            bot.send(answer);
        }
    }
}
