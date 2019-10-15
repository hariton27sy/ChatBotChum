package com.ChatBot.Core;

import java.util.HashSet;

public class Context {
    private HashSet<Ingredient> ingredients;
    private HashSet<Integer> recipesIds;

    public Context(){
        ingredients = new HashSet<>();
        recipesIds = new HashSet<>();
    }

    public int addIngredientAndGetRecipesCount(Ingredient ingredient) {
        if (recipesIds.isEmpty() && ingredients.isEmpty())
            recipesIds.addAll(ingredient.dishesIds);
        ingredients.add(ingredient);
        recipesIds.retainAll(ingredient.dishesIds);
        return recipesIds.size();
    }

    public HashSet<Integer> getRecipesIds(){
        return recipesIds;
    }
}
