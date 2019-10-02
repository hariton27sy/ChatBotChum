package com.ChatBot.Core;

import java.util.HashSet;
import java.util.Set;

public class Ingredient {
    public final String name;
    public HashSet<Recipe> usedIn;

    public Ingredient(String ingredientName){
        name = ingredientName;
    }
}
