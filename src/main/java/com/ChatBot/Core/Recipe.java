package com.ChatBot.Core;

import com.ChatBot.DataBases.IDataStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Recipe {
    public String name;
    public String[] tags;
    public HashSet<Integer> ingredients;
    public HashMap<Integer, int[]> analogs;
    public String link;

    @Override
    public String toString(){
        return name;
    }

    public String getRecipeDescription(IDataStorage database){
        StringBuilder answer = new StringBuilder(name + "\n");
        int counter = 1;
        for(Integer ingredient : ingredients)
            answer.append("   ")
                    .append(counter++)
                    .append(". ")
                    .append(database.getIngredientById(ingredient).name)
                    .append("\n");
        answer.append(link).append('\n');
        return answer.toString();
    }

    public Recipe(String name){
        this.name = name;
        ingredients = new HashSet<>();
        analogs = new HashMap<>();
        tags = new String[0];
    }
}
