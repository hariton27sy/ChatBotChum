package com.ChatBot.Core;

import com.ChatBot.DataBases.DataStorage;

import java.security.InvalidKeyException;
import java.util.HashSet;

public class BotLogic {
    public static DataStorage database = new DataStorage();


    public static String analyzeAndGetAnswer(UserInfo user, Message parsedMessage) {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                    "2. Найди рецепт : формирует запрос на поиск рецепта.\n" +
                    "3. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n";
            //TODO: поиск по тегам.
        }
        else if (parsedMessage.command.equalsIgnoreCase("Покажи")){
            if(user.getContext() == null){
                return database.getRandomRecipe().toString();
            }
            else{
                StringBuilder answer = new StringBuilder();
                for(Recipe recipe : user.getContext().getCurrentSearchResult())
                    answer.append(recipe.toString());
                user.clearContext();
                return answer.toString();
            }
        }
        else if (parsedMessage.command.equalsIgnoreCase("Найди")){
            user.initContext(database.getAllRecipes());
            return "Что вы хотите найти?";
        }
        else if (parsedMessage.command.equalsIgnoreCase("Добавь")){
            Ingredient ingredient;
            try {
                ingredient = database.getIngredientByName(parsedMessage.args[0]);
            } catch (InvalidKeyException e) {
                return "Такого ингредиента в нашей базе не найдено :(";
            }
            HashSet<Recipe> newRecipeSet = ingredient.usedIn;
            newRecipeSet.retainAll(user.getContext().getCurrentSearchResult());
            user.getContext().setCurrentSearchResult(newRecipeSet);
            return String.format("По текущему запросу найдено %s блюд. Хотите добавить что-то ещё?", user.getContext().getCurrentSearchResult().size());
        }
        else{
            return "Я не понял вопроса :-( \nПопробуй написать \"/help\" и узнать о моих возможностях!";
        }
    }
}
