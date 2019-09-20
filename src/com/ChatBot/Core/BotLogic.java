package com.ChatBot.Core;

public class BotLogic {

    public static String analyzeAndGetAnswer(String username, String stringMessage) {
        var parsedMessage = MessageParser.parse(stringMessage);



        return MessageAnalyzer.analyse(UserInfo.getUserInfo(username), parsedMessage);
    }
}
