package com.ChatBot.Core;

public class BotLogic {

    public static String analyzeAndGetAnswer(UserInfo user, Message parsedMessage) {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "1. Покажи рецепт : показывает случайный рецепт.";
        }
        else if (parsedMessage.originalMessage.equalsIgnoreCase("Покажи рецепт")){
            return "Бутерброд с колбасой.\nИнгредиенты:\n   1.Колбаса\n   2.Хлеб";
        }
        else{
            return "Я не понял вопроса :-( \nПопробуй написать \"/help\" и узнать о моих возможностях!";
        }
    }
}
