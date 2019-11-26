package com.ChatBot.DataBases;

import com.ChatBot.Core.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class JSONDataStorage implements IDataStorage {
    private HashMap<Integer, Ingredient> ingredients;
    private HashMap<String, Integer> ingredientsToIds;
    private HashMap<String, Integer> usernamesToIds;
    private HashMap<Integer, UserInfo> users;
    private int maxUsersId;
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
        System.out.println("Path of Database: " + this.JSONDirectory);
        Type type = new TypeToken<HashMap<Integer, Ingredient>>(){}.getType();
        var data = getFileData(this.JSONDirectory + "/Dishes/Ingredients.JSON");
        ingredients = gson.fromJson(data, type);
        ingredientsToIds = new HashMap<>();
        for (int id : ingredients.keySet())
            ingredientsToIds.put(ingredients.get(id).name, id);
        data = getFileData(this.JSONDirectory + "/Dishes/Dishes.JSON");
        type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        recipeNames = gson.fromJson(data, type);
        maxRecipesId = recipeNames.size();
        loadUsers();
        maxUsersId = usernamesToIds.size();
    }

    public JSONDataStorage() {
        this("./JSON");
    }

    private void loadUsers(){
        var data = getFileData(JSONDirectory + "/Users/Users.JSON");
        var type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        usernamesToIds = gson.fromJson(data, type);

    }

    private static String getFileData(String filePath) {
        try {
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
    public Recipe getRecipeByRequest(Context context) {
        var recipesIds = context.getRecipesIds();
        if (recipesIds.size() == 0)
            return null;
        int random = (int) (Math.random() * recipesIds.size());
        Integer[] recipesList = new Integer[recipesIds.size()];
        recipesIds.toArray(recipesList);
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
        if (users == null)
            users = new HashMap<>();

        if (usernamesToIds.containsKey(username))
            return getUserInfo(usernamesToIds.get(username));

        var user = new UserInfo(username);

        users.put(maxUsersId, user);
        usernamesToIds.put(username, maxUsersId++);

        return user;
    }

    @Override
    public UserInfo getUserInfo(int userId) {
        if (userId < maxUsersId){
            if (!users.containsKey(userId)){
                var path = JSONDirectory + "/Users/" + userId + ".JSON";
                users.put(userId, gson.fromJson(getFileData(path), UserInfo.class));
            }

            return users.get(userId);
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public String[] getAllIngredients() {
        var result = new String[ingredients.size()];
        int index = 0;
        for (String key : ingredientsToIds.keySet())
            result[index++] = key;
        return result;
    }

    @Override
    public void updateUsers(UserInfo user) {
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

    @Override
    public Ingredient getIngredientById(int id){
        if(ingredients.containsKey(id))
            return ingredients.get(id);
        throw new NoSuchElementException("No ingredient exists by given ID");
    }

    @Override
    public void close() {

    }

    public int addIngredientAndGetId(String ingredientName){
        if (ingredientsToIds.containsKey(ingredientName))
            return ingredientsToIds.get(ingredientName);

        var ingr = new Ingredient(ingredientName);
        ingredientsToIds.put(ingredientName, ingredientsToIds.size() + 1);
        ingredients.put(ingredientsToIds.get(ingredientName), ingr);
        return ingredientsToIds.get(ingredientName);
    }

    public void addRecipe(Recipe recipe){
        if (recipeNames.containsKey(recipe.name))
            return;
        recipeNames.put(recipe.name, recipeNames.size() + 1);
        for (int ingrId : recipe.ingredients){
            ingredients.get(ingrId).addDish(recipeNames.get(recipe.name));
        }

        var text = gson.toJson(recipe);
        try {
            var path = JSONDirectory + "/Dishes/" + recipeNames.get(recipe.name) + ".JSON";
            var writer = new FileWriter(path, StandardCharsets.UTF_8);
            writer.write(text);
            writer.flush();
            recipeNames.put(recipe.name, recipeNames.get(recipe.name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateFiles() {
        String ingredientsPath = JSONDirectory + "/Dishes/Ingredients.JSON";
        String ingredientToIDPath = JSONDirectory + "/Dishes/IngredientsToID.JSON";

        var ingredients = gson.toJson(this.ingredients);

        try {
            var writer = new FileWriter(ingredientsPath, StandardCharsets.UTF_8);
            writer.write(ingredients);
            writer.flush();

            var ingrToId = gson.toJson(ingredientsToIds);

            writer = new FileWriter(ingredientToIDPath);
            writer.write(ingrToId);
            writer.flush();

            var dishes = gson.toJson(recipeNames);
            writer = new FileWriter(JSONDirectory + "/Dishes/Dishes.JSON");
            writer.write(dishes);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
