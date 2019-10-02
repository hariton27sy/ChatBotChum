package com.ChatBot.DataBases;

import com.ChatBot.Core.Ingredient;
import com.ChatBot.Core.Recipe;
import com.ChatBot.Core.UserInfo;
import com.ChatBot.DataBases.JSONLoaders.JsonRecipeDataLoader;
import com.ChatBot.DataBases.JSONLoaders.JsonUserDataLoader;

import java.security.InvalidKeyException;
import java.util.HashSet;
import java.util.Random;

public class DataStorage {

    private static RecipeDataStorage recipes;
    private static UserDataStorage users;

    public DataStorage(){
        loadDatabase(new JsonRecipeDataLoader(), new JsonUserDataLoader());
    }

    private void loadDatabase(IRecipeDataLoader recipeLoader, IUserDataLoader userLoader){
        recipes = new RecipeDataStorage(recipeLoader.loadRecipes());
        users = new UserDataStorage(userLoader.loadUsers());
    }

    public Recipe getRandomRecipe(){
        Random random = new Random();
        int counter = random.nextInt(recipes.getAllRecipes().size());
        Recipe randomRecipe = null;
        for(Recipe recipe : recipes.getAllRecipes())
            if(--counter == 0)
                randomRecipe = recipe;
        return randomRecipe;
    }

    public HashSet<Recipe> getAllRecipes(){
        return recipes.getAllRecipes();
    }

    //TODO: TryGetIngredient...
    public Ingredient getIngredientByName(String ingredientName) throws InvalidKeyException {
        if(recipes.getAllIngredients().containsKey(ingredientName.toLowerCase()))
            return recipes.getAllIngredients().get(ingredientName.toLowerCase());
        throw new InvalidKeyException();
    }

    public UserInfo getUser(String username){
        return users.getAllUsers().get(username);
    }
}
