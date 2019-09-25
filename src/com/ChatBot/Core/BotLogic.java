package com.ChatBot.Core;

public class BotLogic {

    public static String analyzeAndGetAnswer(UserInfo user, Message parsedMessage) {
        return MessageAnalyzer.analyse(user, parsedMessage);
    }
}
