package com.ChatBot.DataBases.ParsersFromSites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class ParseFromRussianFoodDotCom {
    public Pattern ingredientsTablePattern = Pattern.compile("");

    public static void main(String[] args) {

    }

    private static String getPageContent(int id) throws IOException {
        var url = new URL("https://www.russianfood.com/recipes/recipe.php?rid=" + id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
        var data = new StringBuilder();
        var line = "";
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }

        return data.indexOf("<div class=\"title_div\"><h2>Типы блюд</h2></div>") == -1 ? null : data.toString();
    }

    private static String[] getIngredients(String page) {
        return null;
    }
}
