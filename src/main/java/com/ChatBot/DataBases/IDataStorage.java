package com.ChatBot.DataBases;

import com.ChatBot.Core.*;

import java.util.HashSet;

public interface IDataStorage {
    Recipe getRandomRecipe();
    Recipe getRecipeByRequest(String username);
    Recipe getRecipeByRequest(Request request);
    Recipe getRecipeByRequest(Context context);
    Recipe getRecipe(String recipeName);
    Recipe getRecipe(int recipeId);
    UserInfo getUserInfo(String username);
    UserInfo getUserInfo(int userId);
    String[] getAllIngredients();
    int getIngredientId(String ingredient);
    int[] getAllIngredientsIds();

    void updateUser(UserInfo user);

    HashSet<Recipe> getAllRecipes();

    Ingredient getIngredientByName(String arg);
    Ingredient getIngredientById(int id);

    void close();
}
