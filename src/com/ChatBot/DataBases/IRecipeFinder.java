package com.ChatBot.DataBases;

import com.ChatBot.Core.Recipe;

public interface IRecipeFinder {
    public Recipe findRecipeByIngredient(String ingredient);
}
