package com.ChatBot.Core;

public class MessageAnalyzer {
    public static String analyse(UserInfo userInfo, Message parsedMessage) {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "HELP";
        }
        else if (parsedMessage.originalMessage.equalsIgnoreCase("/")){
            return "something else";
        }
        else{
            return "Я не понял вопроса :-(";
        }

    }
}
