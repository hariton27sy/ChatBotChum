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
                command = Commands.SHOW;
                break;
            case "найди":
                command = Commands.FIND;
                break;
            case "добавь":
                command = Commands.ADD;
                break;
            case "очисти":
                command = Commands.CLEAR_REQUEST;
                break;
            case "ингредиенты":
                command = Commands.INGREDIENTS;
                break;
            case "добавлено":
                command = Commands.ADDED;
                break;
            case "удали":
                command = Commands.REMOVE;
                break;
            case "выйти": case "выйди":
                command = Commands.QUIT;
                break;
            default:
                command = Commands.UNKNOWN;
        }
        args = new String[splittedMessage.length - 1];
        System.arraycopy(splittedMessage, 1, args, 0, args.length);
    }
}
