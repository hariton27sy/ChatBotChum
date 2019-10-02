package com.ChatBot.DataBases.JSONLoaders;

import com.ChatBot.Core.Recipe;
import com.ChatBot.DataBases.IRecipeDataLoader;
import com.ChatBot.DataBases.RecipeDataStorage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.HashSet;

public class JsonRecipeDataLoader implements IRecipeDataLoader {
    @Override
    public HashSet<Recipe> loadRecipes() {
        return new HashSet<>();
    }
}
