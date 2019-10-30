package com.ChatBot.Core;

import java.time.LocalDateTime;

public class Message {
    public final LocalDateTime time;
    public final String originalMessage;
    public final Commands command;
    public final String[] args;
    //TODO: intent?

    public Message(String originalMessage){
        this.originalMessage = originalMessage;
        time = LocalDateTime.now();
        String[] splittedMessage = originalMessage.split(" ", 2);
        switch (splittedMessage[0].toLowerCase()){
            case "покажи":
                command = Commands.Show;
                break;
            case "найди":
                command = Commands.Find;
                break;
            case "добавь":
                command = Commands.Add;
                break;
            case "очисти":
                command = Commands.ClearRequest;
                break;
            case "ингредиенты":
                command = Commands.Ingredients;
                break;
            case "добавлено":
                command = Commands.Added;
                break;
            case "выйти": case "выйди":
                command = Commands.Quit;
                break;
            default:
                command = Commands.Unknown;
        }
        args = new String[splittedMessage.length - 1];
        System.arraycopy(splittedMessage, 1, args, 0, args.length);
    }
}
