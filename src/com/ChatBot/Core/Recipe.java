package com.ChatBot.Core;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Recipe {
    public String name;
    public HashSet<String> tags;
    public HashSet<Ingredient> ingredients;

    @Override
    public String toString(){
        StringBuilder answer = new StringBuilder(name + "\n");
        for(Ingredient ingredient : ingredients)
            answer.append(ingredient.name).append("\n");
        return answer.toString();
    }
}
