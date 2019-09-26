package com.ChatBot.DataBases;

import com.ChatBot.Core.Recipe;

import java.util.List;
import java.util.Map;

import org.json.*;

public class JsonRecipeFinder implements IRecipeFinder {
    private Map<String, Recipe> recipes;

    public JsonRecipeFinder(){
        var json = new JSONObject();
    }

    @Override
    public Recipe findRecipeByIngredient(String ingredient) {
        return null;
    }
}
