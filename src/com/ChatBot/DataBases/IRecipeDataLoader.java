package com.ChatBot.DataBases;

import com.ChatBot.Core.Ingredient;
import com.ChatBot.Core.Recipe;

import java.util.HashSet;

public interface IRecipeDataLoader {
    public HashSet<Recipe> loadRecipes();
    //TODO: public void addRecipe();
}
