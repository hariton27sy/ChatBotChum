package com.ChatBot.Core;

import com.ChatBot.DataBases.IDataStorage;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class BotLogic implements IBotLogic {
    private IDataStorage database;

    private HashMap<String, HashSet<String>> shownRecipes = new HashMap<>();

    public BotLogic(IDataStorage dataStorage){
        database = dataStorage;
    }

    public String analyzeAndGetAnswer(String username, Message parsedMessage)
            throws Exception {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                    "2. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n" +
                    "3. Добавлено : показывает добавленные в запрос ингредиенты.\n" +
                    "4. Очисти запрос: очищает список добавленных ингредиентов\n" +
                    "5. Удали <индекс/название ингредиента> : удаляет ингредиент из запроса";
        }

        if (parsedMessage.originalMessage.equalsIgnoreCase("/start")){
            return "Я - шефот, и могу помочь тебе выбрать блюдо на вечер. Или на утро. Или перекус.\n" +
                    "В общем, не стесняйся, говори, что ты хочешь, а я подскажу ;)\n" +
                    "Для того, чтобы узнать, как со мной работать, напиши '/help'";
        }
        UserInfo user = database.getUserInfo(username);
        switch (parsedMessage.command){
            case ADD:
                clearShownRecipesFor(user);
                if (parsedMessage.args.length == 0)
                    return "Я не могу добавить пустой ингредиент :( Пожалуйста введи название ингредиента";
                Ingredient ingredient = database.getIngredientByName(parsedMessage.args[0]);
                if (ingredient == null)
                    return "Я не знаю такого ингредиента, попробуй изменить название :(";
                if (user.getContext() == null)
                    user.initContext();
                int count = user.getContext().addIngredientAndGetRecipesCount(ingredient);
                database.updateUser(user);
                return String.format("По текущему запросу:\n%s\nя нашёл %s блюд. Хочешь добавить что-то ещё?",
                        user.getContext().ingredientsListToString(), count);
            case SHOW:
                if(user.getContext() == null){
                    return database.getRandomRecipe().getRecipeDescription(database);
                }
                else if (user.getContext().getRecipesCount() == 0){
                    return "По текущему запросу я ничего не нашёл. Попробуй изменить запрос, или составить новый :)";
                }
                else if (getShownRecipesCountFor(user) < user.getContext().getRecipesCount()){
                    Recipe answer = getUnshownRecipeFor(user);
                    addToShownRecipes(user, answer.name);
                    return answer.getRecipeDescription(database);
                }
                else{
                    clearShownRecipesFor(user);
                    user.clearContext();
                    database.updateUser(user);
                    return "Я показал все блюда по текущему запросу. Давай сформируем новый =^_^=";
                }
            case FIND:
                return "Я не работаю, я ем";
            case INGREDIENTS:
                return String.join("\n", database.getAllIngredients());
            case UNKNOWN:
                return "Я не понял вопроса :-( \nПопробуй написать \"/help\" и узнать о моих возможностях!";
            case ADDED:
                var context = user.getContext();
                if (context == null || context.ingredientsListToString().equals(""))
                    return "Ты пока не добавил ни одного ингредиента в запрос. Скорее же сделай это";
                return "Пока ты добавил следующие ингредиенты:\n" + context.ingredientsListToString();
            case CLEAR_REQUEST:
                clearShownRecipesFor(user);
                user.clearContext();
                database.updateUser(user);
                return "Поисковый запрос пуст";
            case REMOVE:
                clearShownRecipesFor(user);
                int index;
                try {
                    index = Integer.parseInt(parsedMessage.args[0]) - 1;
                    int amount = user.getContext().removeIngredientAndGetRecipesCount(index);
                    database.updateUser(user);
                    return String.format("По текущему запросу:\n%s\nя нашёл %s блюд. Хочешь добавить что-то ещё?",
                            user.getContext().ingredientsListToString(),
                            amount);
                } catch (Exception exc) {
                    try {
                        int amount = user.getContext().removeIngredientAndGetRecipesCount(parsedMessage.args[0]);
                        database.updateUser(user);
                        return String.format("По текущему запросу:\n%s\nя нашёл %s блюд. Хочешь добавить что-то ещё?",
                                user.getContext().ingredientsListToString(),
                                amount);
                    } catch (Exception exc2) {
                        return "Неверный ингредиент";
                    }
                }
            case QUIT:
                return "Q";
            default:
                throw new Exception();
        }
    }

    private void addToShownRecipes(UserInfo user, String recipeName){
        shownRecipes.computeIfAbsent(user.username, k -> new HashSet<>());
        shownRecipes.get(user.username).add(recipeName);
    }

    private void clearShownRecipesFor(UserInfo user){
        if(shownRecipes.get(user.username) != null)
            shownRecipes.put(user.username, null);
    }

    private int getShownRecipesCountFor(UserInfo user){
        if(shownRecipes.get(user.username) != null)
            return shownRecipes.get(user.username).size();
        return 0;
    }

    private Recipe getUnshownRecipeFor(UserInfo user){
        Recipe unshownRecipe = database.getRecipeByRequest(user.getContext());
        while (shownRecipes.get(user.username) != null &&
                shownRecipes.get(user.username).contains(unshownRecipe.name))
            unshownRecipe = database.getRecipeByRequest(user.getContext());
        return unshownRecipe;
    }

    public Collection<String> getAddedIngredients(String username){
        UserInfo user = database.getUserInfo(username);
        return user.getContext().getIngredientsAsStringCollection();
    }

    @Override
    public void stop() {

    }
}
