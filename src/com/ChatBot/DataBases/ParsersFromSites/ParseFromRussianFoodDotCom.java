package com.ChatBot.DataBases.ParsersFromSites;

import org.json.*;

public class ParseFromRussianFoodDotCom {

    public Recipe[] parseFood(){
        Recipe[] recipes = new Recipe[3];
        recipes[0] = new Recipe(1,
                "Суфле из кукурузной муки с перцем и сыром",
                "Рецепты сладостей",
                "https://www.russianfood.com/recipes/recipe.php?rid=117028");
        recipes[1] = new Recipe(2,
                "Суфле \\\"Птичье молоко\\\"",
                "Рецепты сладостей",
                "https://www.russianfood.com/recipes/recipe.php?rid=59615");
        recipes[2] = new Recipe(3,
                "Сливочно-творожный крем с шоколадом",
                "Рецепты сладостей",
                "https://www.russianfood.com/recipes/recipe.php?rid=144841");
        recipes[0].ingredients_ids = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
        recipes[1].ingredients_ids = new int[]{11, 12, 13, 14};
        recipes[2].ingredients_ids = new int[]{6, 12, 14, 15, 16, 17};
        recipes[0].analogs = new Recipe[]{recipes[1]};
        return recipes;
    }

    public class Recipe{
        public int id;
        public String name;
        public String type;
        public int[] ingredients_ids;
        public Recipe[] analogs;
        public String link;

        public Recipe(int id, String name, String type, String link){
            this.id = id;
            this.name = name;
            this.type = type;
            this.link = link;
        }
    }
}
