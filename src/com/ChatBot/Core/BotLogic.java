package com.ChatBot.Core;

import com.ChatBot.DataBases.IDataStorage;
import com.ChatBot.DataBases.JSONDataStorage;
import jdk.jshell.spi.ExecutionControl;

public class BotLogic {
    private static IDataStorage database = new JSONDataStorage();


    public static String analyzeAndGetAnswer(UserInfo user, Message parsedMessage)
            throws ExecutionControl.NotImplementedException {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                    "2. Найди рецепт : формирует запрос на поиск рецепта.\n" +
                    "3. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n";
            //TODO: поиск по тегам.
        }
        switch (parsedMessage.command){
            case Add:
                Ingredient ingredient = database.getIngredientByName(parsedMessage.args[0]);
                if (ingredient == null)
                    return "Я не знаю такого ингредиента, попробуйте изменить название :(";
                if (user.getContext() == null)
                    user.initContext();
                var count = user.getContext().addIngredientAndGetRecipesCount(ingredient);
                return String.format("По текущему запросу найдено %s блюд. Хотите добавить что-то ещё?", count);
            case Show:
                if(user.getContext() == null){
                    return database.getRandomRecipe().toString();
                }
                else{
                    var answer = database.getRecipeByRequest(user.getContext());
                    return answer.toString();
                }
            case Find:
                return "Заглушка";
            case Unknown:
                return "Я не понял вопроса :-( \nПопробуй написать \"/help\" и узнать о моих возможностях!";
            default:
                throw new ExecutionControl.NotImplementedException("");
        }
    }
}
