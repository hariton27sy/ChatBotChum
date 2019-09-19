package com.ChatBot;

import com.ChatBot.Interfaces.ConsoleInterface;
import com.ChatBot.Interfaces.InOutInterface;
import com.ChatBot.Interfaces.TelegramInterface;

public class Chefot {
    InOutInterface bot;

    public void main(String[] args) {
        bot = new TelegramInterface();
        while(true){
            String userInput = bot.Receive();
            String answer = BotLogic.AnalyzeAndGetAnswer(MessageParser.ParseMessage(userInput));
            bot.Send(answer);
        }




        if (args.length == 0) {
            var inout = new ConsoleInterface();
            inout.main(new String[0]);
        }
        else {
            System.out.println("Help");
        }
    }
}
