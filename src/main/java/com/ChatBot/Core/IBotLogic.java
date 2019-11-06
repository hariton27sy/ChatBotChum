package com.ChatBot.Core;

public interface IBotLogic {
    public String analyzeAndGetAnswer(String username, Message parsedMessage) throws Exception;
    public void stop();
}
