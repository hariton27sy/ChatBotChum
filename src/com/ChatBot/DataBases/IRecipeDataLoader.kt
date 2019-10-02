package com.ChatBot.DataBases

import com.ChatBot.Core.Ingredient
import com.ChatBot.Core.Recipe

import java.util.HashSet

interface IRecipeDataLoader {
    fun loadRecipes(): HashSet<Recipe>
    //TODO: public void addRecipe();
}
