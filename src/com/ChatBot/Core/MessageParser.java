package com.ChatBot.Core;

public class MessageParser {
    public static Message parseMessage(String userInput) {
        return new Message(userInput);
    }
}
