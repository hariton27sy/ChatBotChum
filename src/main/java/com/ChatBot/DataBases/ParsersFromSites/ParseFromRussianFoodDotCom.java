package com.ChatBot.DataBases.ParsersFromSites;

import com.ChatBot.Core.Ingredient;
import com.ChatBot.Core.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ParseFromRussianFoodDotCom {
    //public Pattern ingredientsTablePattern = Pattern.compile("");
    public static String dishesPath = "JSON/Dishes";

    public static HashMap<Integer, Ingredient> ingredients;
    public static HashMap<String, Integer> ingredientsToIds;
    public static HashMap<String, Integer> recipeToIds;
    public static int recipeId;
    public static int ingrId;

    private static Pattern titlePattern = Pattern.compile("<h1 class=\"title \">(.*)</h1>");

    public static void main(String[] args) throws IOException {
        loadDataBase();
        var page = getPageContent(1);
        parsePage(page);
    }

    private static String getPageContent(int id) throws IOException {
        var url = new URL("https://www.russianfood.com/recipes/recipe.php?rid=" + id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "Windows-1251"));
        var data = new StringBuilder();
        var line = "";
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }
        var str = data.toString();

        return data.toString().contains("<div class=\"title_div\"><h2>Типы блюд</h2></div>") ? null : data.toString();
    }

    private static String[] getIngredients(String page) {
        return null;
    }

    private static void loadDataBase(){
        var gson = new Gson();

        dishesPath = FileSystems.getDefault().getPath(dishesPath).normalize().toAbsolutePath().toString();
        Type type = new TypeToken<HashMap<Integer, Ingredient>>(){}.getType();
        var data = getFileData(dishesPath + "/Ingredients.JSON");
        ingredients = gson.fromJson(data, type);
        ingredientsToIds = new HashMap<>();
        for (int id : ingredients.keySet())
            ingredientsToIds.put(ingredients.get(id).name, id);
        data = getFileData(dishesPath + "/Dishes.JSON");
        type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        recipeToIds = gson.fromJson(data, type);
        recipeId = recipeToIds.size();
        ingrId = ingredients.size();
    }

    private static String getFileData(String filePath) {
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

    private static void parsePage(String data) {
        var recipe = new Recipe();
        var matcher = titlePattern.matcher(data);
        recipe.name = data.substring(matcher.start(), matcher.end());
        System.out.println(recipe.name);
    }
}
