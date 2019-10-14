package com.ChatBot.Core;

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
        /*StringBuilder answer = new StringBuilder(name + "\n");
        for(Ingredient ingredient : ingredients)
            answer.append(ingredient.name).append("\n");
        return answer.toString();*/
        return name;
    }

    public Recipe(){ }

    public Recipe(String name, String[] tags, HashSet<Integer> ingredients,
                  HashMap<Integer, int[]> analogs, String link){
        this.name = name;
        this.tags = tags;
        this.ingredients = ingredients;
        this.analogs = analogs;
        this.link = link;
    }
}
