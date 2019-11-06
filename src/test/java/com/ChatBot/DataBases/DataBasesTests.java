package com.ChatBot.DataBases;
import com.ChatBot.Core.*;
import com.ChatBot.DataBases.JSONDataStorage;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DataBasesTests {

    @Test
    void GetRandomRecipeTest(){
        JSONDataStorage database = new JSONDataStorage();
        Recipe recipe = database.getRandomRecipe();
        Assert.assertNotNull(recipe);
        Assert.assertTrue(recipe.name.length() > 0);
    }

    @Test
    void GetIngredientByNameTest(){
        JSONDataStorage database = new JSONDataStorage();
        for(int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * database.getAllIngredients().length);
            Ingredient randomIngredient = database.getIngredientByName(database.getAllIngredients()[random]);
            Assert.assertNotNull(randomIngredient);
            Assert.assertTrue(randomIngredient.name.length() > 0);
            Assert.assertEquals(database.getAllIngredients()[random], randomIngredient.name);
        }
    }

    @Test
    void GetRecipeByRequestTest(){
        JSONDataStorage database = new JSONDataStorage();
        for(int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * database.getAllIngredients().length) + 1;
            ArrayList<Integer> ingredientList = new ArrayList<>();
            ingredientList.add(random);
            Request request = new Request(ingredientList);
            Recipe recipe = database.getRecipeByRequest(request);
            Assert.assertNotNull(recipe);
            Assert.assertTrue(recipe.name.length() > 0);
        }
    }
}
