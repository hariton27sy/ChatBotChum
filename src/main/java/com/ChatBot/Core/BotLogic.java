package com.ChatBot.Core;

import com.ChatBot.DataBases.IDataStorage;

public class BotLogic implements IBotLogic {
    private IDataStorage database;

    public BotLogic(IDataStorage dataStorage){
        database = dataStorage;
    }

    public String analyzeAndGetAnswer(String username, Message parsedMessage)
            throws Exception {
        if(parsedMessage.originalMessage.equalsIgnoreCase("/help")){
            return "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                    "2. Найди рецепт : формирует запрос на поиск рецепта. (Не работает)\n" +
                    "3. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n" +
                    "4. Ингредиенты : показывает все ингредиенты, которые я знаю.\n" +
                    "5. Добавлено : показывает добавленные в запрос ингредиенты.\n" +
                    "6. Очисти запрос: очищает список добавленных ингредиентов\n" +
                    "7. Выйди / выйти: выходит из программы.\n" +
                    "8. Удали <индекс ингредиента> : удаляет ингредиент из запроса";
            //TODO: поиск по тегам.
        }
        var user = database.getUserInfo(username);
        switch (parsedMessage.command){
            case Add:
                if (parsedMessage.args.length == 0)
                    return "Я не могу добавить пустой ингредиент :( Пожалуйста введите название ингредиента";
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
                    user.clearContext();
                    if (answer == null) {
                        return "По такому запросу блюд не найдено :( Попробуйте изменить запрос.";
                    }
                    return answer.toString();
                }
            case Find:
                return "Я не работаю, я ем";
            case Ingredients:
                return String.join("\n", database.getAllIngredients());
            case Unknown:
                return "Я не понял вопроса :-( \nПопробуй написать \"/help\" и узнать о моих возможностях!";
            case Added:
                var context = user.getContext();
                if (context == null || context.ingredientsListToString().equals(""))
                    return "Вы пока не добавили ни одного ингредиента в запрос. Скорее же сделайте это";
                return "Пока вы добавили следующие ингредиенты:\n" + context.ingredientsListToString();
            case ClearRequest:
                user.clearContext();
                return "Поисковый запрос пуст";
            case Remove:
                int index;
                try{
                    index = Integer.parseInt(parsedMessage.args[0]);
                }
                catch (Exception exc){
                    return "Неверный индекс ингредиента";
                }
                return String.format("По текущему запросу найдено %s блюд. Хотите добавить что-то ещё?",
                        user.getContext().removeIngredientAndGetRecipesCount(index));
            case Quit:
                return "Q";
            default:
                throw new Exception();
        }
    }

    @Override
    public void stop() {

    }
}
