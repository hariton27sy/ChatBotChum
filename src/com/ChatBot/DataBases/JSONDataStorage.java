package com.ChatBot.DataBases;

import com.ChatBot.Core.Ingredient;
import com.ChatBot.Core.Recipe;
import com.ChatBot.Core.Request;
import com.ChatBot.Core.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.HashSet;

public class JSONDataStorage implements IDataStorage {
    private HashMap<Integer, Ingredient> ingredients;
    private HashMap<String, Integer> ingredientsToIds;
    private HashMap<String, UserInfo> users;
    private int maxRecipesId;
    private String JSONDirectory;
    private Gson gson;
    private HashMap<String, Integer> recipeNames;

    public JSONDataStorage(String JSONDirectory){
        /*GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Recipe.class, new RecipeDeserialize());
        gson = builder.create();*/
        gson = new Gson();

        this.JSONDirectory = FileSystems.getDefault().getPath(JSONDirectory).normalize().toAbsolutePath().toString();
        Type type = new TypeToken<HashMap<Integer, Ingredient>>(){}.getType();
        var data = getFileData(this.JSONDirectory + "/Dishes/Ingredients.JSON");
        ingredients = gson.fromJson(data, type);
        type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        ingredientsToIds = gson.fromJson(getFileData(this.JSONDirectory + "/Dishes/IngredientToID.JSON"), type);
        data = getFileData(this.JSONDirectory + "/Dishes/Dishes.JSON");
        type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        recipeNames = gson.fromJson(data, type);
        maxRecipesId = recipeNames.size();
        loadUsers();
    }

    public JSONDataStorage() {
        this("./JSON");
    }

    private void loadUsers(){
        var data = getFileData(JSONDirectory + "/Users/Users.JSON");
        var type = new TypeToken<HashMap<String, UserInfo>>(){}.getType();
        users = gson.fromJson(data, type);

    }

    public static String getFileData(String filePath) {
        try{
            var stream = new BufferedReader(new FileReader(filePath));
            var result = new StringBuilder();
            var line = "";
            while ((line = stream.readLine()) != null){
                result.append(line);
            }

            return result.toString();
        } catch (IOException e) {
            return "";
        }


    }

    @Override
    public Recipe getRandomRecipe() {
        var random = (int) (1 + Math.round(Math.random() * (maxRecipesId - 1)));
        return getRecipe(random);
    }

    @Override
    public Recipe getRecipeByRequest(String username) {
        return null;
    }

    @Override
    public Recipe getRecipeByRequest(Request request) {
        HashSet<Integer> recipes = null;
        for (Integer ingredientId : request.ingredientIds){
            if (recipes == null) {
                recipes = ingredients.get(ingredientId).dishesIds;
            }
            else
                recipes.retainAll(ingredients.get(ingredientId).dishesIds);
        }

        assert recipes != null;
        if (recipes.isEmpty())
            return null;

        int random = (int) (Math.random() * recipes.size());
        Integer[] recipesList = new Integer[recipes.size()];
        recipes.toArray(recipesList);
        return getRecipe(recipesList[random]);
    }

    @Override
    public Recipe getRecipe(String recipeName) {
        if (recipeNames.containsKey(recipeName)){
            return getRecipe(recipeNames.get(recipeName));
        }
        return null;
    }

    @Override
    public Recipe getRecipe(int recipeId) {
        if (recipeId <= maxRecipesId && recipeId > 0){
            var path = JSONDirectory + "/Dishes/" + recipeId + ".JSON";
            return gson.fromJson(getFileData(path), Recipe.class);
        }

        return null;
    }

    @Override
    public UserInfo getUserInfo(String username) {
        if (users.containsKey(username))
            return users.get(username);

        return null;
    }

    @Override
    public UserInfo getUserInfo(int userId) {
        return null;
    }

    @Override
    public void updateUserInfo(UserInfo user) {
        
    }

    @Override
    public HashSet<Recipe> getAllRecipes() {
        var result = new HashSet<Recipe>();
        for (String name : recipeNames.keySet())
            result.add(getRecipe(name));

        return result;
    }

    @Override
    public Ingredient getIngredientByName(String arg) {
        if (ingredientsToIds.containsKey(arg))
            return ingredients.get(ingredientsToIds.get(arg));

        return null;
    }
}
