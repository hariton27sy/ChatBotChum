package com.ChatBot.DataBases;

import com.ChatBot.Core.Ingredient;
import com.ChatBot.Core.Recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RecipeDataStorage {
    private HashSet<Recipe> recipes;
    private HashMap<String, Ingredient> ingredients;

    public RecipeDataStorage(HashSet<Recipe> recipes){
        this.recipes = recipes;
        ingredients = new HashMap<>();
        for(Recipe recipe : recipes){
            for(Ingredient ingredient : recipe.ingredients){
                if(!ingredients.containsKey(ingredient.name)){
                    ingredients.put(ingredient.name, new Ingredient(ingredient.name));
                }
                ingredients.get(ingredient.name).usedIn.add(recipe);
            }
        }
    }

    public HashSet<Recipe> getAllRecipes(){
        return recipes;
    }

    public Map<String, Ingredient> getAllIngredients(){
        return ingredients;
    }
}
