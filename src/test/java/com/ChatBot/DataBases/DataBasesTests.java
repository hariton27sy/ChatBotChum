package com.ChatBot.DataBases;
import com.ChatBot.Core.*;
import com.ChatBot.DataBases.JSONDataStorage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class DataBasesTests {

    private static ArrayList<IDataStorage> databases;

    @BeforeAll
    static void setUp(){
        databases = new ArrayList<>();
        databases.add(new JSONDataStorage());
        try {
            databases.add(new MySQLDataBase());
        }
        catch (Exception e){
            System.out.println("SQL database was not initialized!");
        }
    }

    @Test
    void getRandomRecipeTest(){
        for(IDataStorage database : databases){
            Recipe recipe = database.getRandomRecipe();
            Assert.assertNotNull(recipe);
            Assert.assertTrue(recipe.name.length() > 0);
        }
    }

    @Test
    void getIngredientByNameTest(){
        for(IDataStorage database : databases){
            String[] ingredients = database.getAllIngredients();
            for(int i = 0; i < 100; i++) {
                int random = (int) (Math.random() * ingredients.length);
                Ingredient randomIngredient = database.getIngredientByName(ingredients[random]);
                Assert.assertNotNull(randomIngredient);
                Assert.assertEquals(ingredients[random], randomIngredient.name);
            }
        }
    }

    @Test
    void getRecipeByRequestTest(){
        for(IDataStorage database : databases){
            int[] ingredients = database.getAllIngredientsIds();
            for(int i = 0; i < 100; i++) {
                int random = (int) (Math.random() * ingredients.length);
                ArrayList<Integer> usedIngredients = new ArrayList<>();
                usedIngredients.add(ingredients[random]);

                Recipe recipe = database.getRecipeByRequest(new Request(usedIngredients));
                Assert.assertNotNull(recipe);
                Assert.assertTrue(recipe.name.length() > 0);
            }
        }
    }
}
