package com.ChatBot.Core;

public class Message {
    public String[] ingredients;
    public String cookingMethod;
    public int time;
    public final String originalMessage;

    public Message(String originalMessage){
        this.originalMessage = originalMessage;
    }
}
