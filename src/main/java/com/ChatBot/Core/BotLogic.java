package com.ChatBot.Core;

import com.ChatBot.DataBases.IDataStorage;

import java.util.Collection;

public class BotLogic implements IBotLogic {
    private IDataStorage database;

    public BotLogic(IDataStorage dataStorage){
        database = dataStorage;
    }

    public String analyzeAndGetAnswer(String username, Message parsedMessage)
            throws Exception {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                    //"2. Найди рецепт : формирует запрос на поиск рецепта. (Не работает)\n" +
                    "2. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n" +
                    "3. Ингредиенты : показывает все ингредиенты, которые я знаю.\n" +
                    "4. Добавлено : показывает добавленные в запрос ингредиенты.\n" +
                    "5. Очисти запрос: очищает список добавленных ингредиентов\n" +
                    "6. Выйди / выйти: выходит из программы.\n" +
                    "7. Удали <индекс/название ингредиента> : удаляет ингредиент из запроса";
            //TODO: поиск по тегам.
        }

        if (parsedMessage.originalMessage.equalsIgnoreCase("/start")){
            return "Я - шефот, и могу помочь тебе выбрать блюдо на вечер. Или на утро. Или перекус.\n" +
                    "В общем, не стесняйся, говори, что ты хочешь, а я подскажу ;)\n" +
                    "Для того, чтобы узнать, как со мной работать, напиши '/help'";
        }
        var user = database.getUserInfo(username);
        switch (parsedMessage.command){
            case ADD:
                if (parsedMessage.args.length == 0)
                    return "Я не могу добавить пустой ингредиент :( Пожалуйста введите название ингредиента";
                Ingredient ingredient = database.getIngredientByName(parsedMessage.args[0]);
                if (ingredient == null)
                    return "Я не знаю такого ингредиента, попробуйте изменить название :(";
                if (user.getContext() == null)
                    user.initContext();
                var count = user.getContext().addIngredientAndGetRecipesCount(ingredient);
                database.updateUser(user);
                return String.format("По текущему запросу:\n%s\nнайдено %s блюд. Хотите добавить что-то ещё?",
                        user.getContext().ingredientsListToString(), count);
            case SHOW:
                if(user.getContext() == null){
                    return database.getRandomRecipe().getNameAndIngredients(database);
                }
                else{
                    var answer = database.getRecipeByRequest(user.getContext());
                    user.clearContext();
                    if (answer == null) {
                        return "По такому запросу блюд не найдено :( Попробуйте изменить запрос.";
                    }
                    return answer.getNameAndIngredients(database);
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
                    return "Вы пока не добавили ни одного ингредиента в запрос. Скорее же сделайте это";
                return "Пока вы добавили следующие ингредиенты:\n" + context.ingredientsListToString();
            case CLEAR_REQUEST:
                user.clearContext();
                database.updateUser(user);
                return "Поисковый запрос пуст";
            case REMOVE:
                int index;
                try {
                    index = Integer.parseInt(parsedMessage.args[0]) - 1;
                    var amount = user.getContext().removeIngredientAndGetRecipesCount(index);
                    database.updateUser(user);
                    return String.format("По текущему запросу:\n%s\nнайдено %s блюд. Хотите добавить что-то ещё?",
                            user.getContext().ingredientsListToString(),
                            amount);
                } catch (Exception exc) {
                    try {
                        var amount =user.getContext().removeIngredientAndGetRecipesCount(parsedMessage.args[0]);
                        database.updateUser(user);
                        return String.format("По текущему запросу:\n%s\nнайдено %s блюд. Хотите добавить что-то ещё?",
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

    public Collection<String> getAddedIngredients(String username){
        UserInfo user = database.getUserInfo(username);
        return user.getContext().getIngredientsAsStringCollection();
    }

    @Override
    public void stop() {

    }
}
