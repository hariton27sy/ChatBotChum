package com.ChatBot.DataBases;

import com.ChatBot.Core.Ingredient;
import com.ChatBot.Core.Recipe;
import com.ChatBot.Core.Request;
import com.ChatBot.Core.UserInfo;

import java.util.HashSet;

public interface IDataStorage {
    public Recipe getRandomRecipe();
    public Recipe getRecipeByRequest(String username);
    public Recipe getRecipeByRequest(Request request);
    public Recipe getRecipe(String recipeName);
    public Recipe getRecipe(int recipeId);
    public UserInfo getUserInfo(String username);
    public UserInfo getUserInfo(int userId);

    public void updateUserInfo(UserInfo user);

    HashSet<Recipe> getAllRecipes();

    Ingredient getIngredientByName(String arg);
}
