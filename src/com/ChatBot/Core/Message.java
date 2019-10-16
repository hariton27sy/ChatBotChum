package com.ChatBot.Core;

import java.time.LocalDateTime;

public class Message {
    public final LocalDateTime time;
    public final String originalMessage;
    public final commands command;
    public final String[] args;
    //TODO: intent?

    public Message(String originalMessage){
        this.originalMessage = originalMessage;
        time = LocalDateTime.now();
        String[] splittedMessage = originalMessage.split(" ");
        switch (splittedMessage[0].toLowerCase()){
            case "покажи":
                command = commands.Show;
                break;
            case "найди":
                command = commands.Find;
                break;
            case "добавь":
                command = commands.Add;
                break;
            case "очисти":
                command = commands.ClearRequest;
                break;
            case "ингредиенты":
                command = commands.Ingredients;
                break;
            default:
                command = commands.Unknown;
        }
        args = new String[splittedMessage.length - 1];
        System.arraycopy(splittedMessage, 1, args, 0, args.length);
        // parseMessage()
    }
}
