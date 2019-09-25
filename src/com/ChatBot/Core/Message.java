package com.ChatBot.Core;

public class Message {
    public String[] ingredients;
    public String cookingMethod;
    public int time;
    public final String originalMessage;

    public Message(String originalMessage){
        this.originalMessage = originalMessage;
        // parseMessage()
    }

    private static Message parseMessage(String userInput) {
        return new Message(userInput);
    }
}
