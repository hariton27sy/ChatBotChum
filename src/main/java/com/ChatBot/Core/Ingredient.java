package com.ChatBot.Core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Ingredient {
    public String name;
    public HashSet<Integer> dishesIds;

    public Ingredient(String name, HashSet<Integer> dishesIds){
        this.name = name;
        this.dishesIds = dishesIds;
    }
}
