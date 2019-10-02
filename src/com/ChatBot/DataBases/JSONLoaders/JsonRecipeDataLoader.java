package com.ChatBot.DataBases.JSONLoaders;

import com.ChatBot.Core.Recipe;
import com.ChatBot.DataBases.IRecipeDataLoader;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class JsonRecipeDataLoader implements IRecipeDataLoader {
    private String recipeDataPath = "../Dishes/Dishes.JSON";

    @Override
    public HashSet<Recipe> loadRecipes() {
        var result = new HashSet<Recipe>();

        Gson gson = new Gson();

        JSONRecipe[] recipes = gson.fromJson(getFileData(recipeDataPath), JSONRecipe[].class);

        return new HashSet<>();
    }

    private String getFileData(String dataBasePath) {
        try {
            var reader = new BufferedReader(new FileReader(dataBasePath));
            String line = null;
            StringBuilder data = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            return data.toString();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}

class JSONRecipe {
    public String name;
    public String[] tags;
    public int[] ingredients_ids;
    public Map<Integer, Integer> analogs;
    public String link;
    public JSONRecipe(String name, String[] tags, int[] ingredients_ids, Map<Integer, Integer> analogs, String link){
        this.name = name;
        this.tags = tags;
        this.ingredients_ids = ingredients_ids;
        this.analogs = analogs;
        this.link = link;
    }
}
