package com.ChatBot.DataBases;

import com.ChatBot.Core.*;

import java.util.HashSet;

public interface IDataStorage {
    public Recipe getRandomRecipe();
    public Recipe getRecipeByRequest(String username);
    public Recipe getRecipeByRequest(Request request);
    public Recipe getRecipeByRequest(Context context);
    public Recipe getRecipe(String recipeName);
    public Recipe getRecipe(int recipeId);
    public UserInfo getUserInfo(String username);
    public UserInfo getUserInfo(int userId);

    public void updateUserInfo(UserInfo user);

    HashSet<Recipe> getAllRecipes();

    Ingredient getIngredientByName(String arg);
}
