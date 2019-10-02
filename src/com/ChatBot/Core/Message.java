package com.ChatBot.Core;

import java.time.LocalDateTime;

public class Message {
    public final LocalDateTime time;
    public final String originalMessage;
    public final String command;
    public final String[] args;
    //TODO: intent?

    public Message(String originalMessage){
        this.originalMessage = originalMessage;
        time = LocalDateTime.now();
        String[] splittedMessage = originalMessage.split(" ");
        command = splittedMessage[0].toLowerCase();
        args = new String[splittedMessage.length - 1];
        System.arraycopy(splittedMessage, 1, args, 0, args.length);
        // parseMessage()
    }
}
