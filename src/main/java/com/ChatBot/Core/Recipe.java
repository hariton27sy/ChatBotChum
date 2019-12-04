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
                    //.append(", ID: ")
                    //.append(ingredient)
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

    public Recipe(String name, String[] tags, HashSet<Integer> ingredients,
                  HashMap<Integer, int[]> analogs, String link){
        this.name = name;
        this.tags = tags;
        this.ingredients = ingredients;
        this.analogs = analogs;
        this.link = link;
    }
}
