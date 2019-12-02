package com.ChatBot.DataBases;
import com.ChatBot.Core.*;
import com.ChatBot.DataBases.JSONDataStorage;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DataBasesTests {

    private ArrayList<IDataStorage> databases;

    @BeforeEach
    void setUp(){
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
            for(int i = 0; i < 100; i++) {
                int random = (int) (Math.random() * database.getAllIngredients().length);
                Ingredient randomIngredient = database.getIngredientByName(database.getAllIngredients()[random]);
                Assert.assertNotNull(randomIngredient);
                Assert.assertEquals(database.getAllIngredients()[random], randomIngredient.name);
            }
        }
    }

    @Test
    void getRecipeByRequestTest(){
        for(IDataStorage database : databases){
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
}
