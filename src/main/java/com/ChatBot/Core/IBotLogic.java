package com.ChatBot.Core;

import java.util.Collection;

public interface IBotLogic {
    public String analyzeAndGetAnswer(String username, Message parsedMessage) throws Exception;
    public void stop();
    public Collection<String> getAddedIngredients(String username);
}
