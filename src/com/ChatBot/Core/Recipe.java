package com.ChatBot.Core;

import java.util.List;
import java.util.Map;

public class Recipe {
    public String name;
    public String type;
    public List<Ingredient> ingredients;
    public Map<Ingredient, List<Ingredient>> analogs;
    public String foodCountry;
}
