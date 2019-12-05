package com.ChatBot.DataBases.Export;

import com.ChatBot.Core.Recipe;
import com.ChatBot.DataBases.IDataStorage;
import com.ChatBot.DataBases.JSONDataStorage;
import com.ChatBot.DataBases.MySQLDataBase;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportToMySQL {
    private IDataStorage dataStorage;
    private MySQLDataBase mysqlDataStorage;
    private int currentIndex;
    private int recId;

    public ImportToMySQL(IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
        currentIndex = 1;
        recId = 1;
        try {
            mysqlDataStorage = new MySQLDataBase("sqlpasswords.txt");
            importRecipes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importIngredients() {
        String ingr = getNextIngredient();
        String query = "";
        while (ingr != null) {
            query = String.format("insert into ingredients (name) values (_utf8 \"%s\");", ingr);
            try {
                mysqlDataStorage.executeQuery(query);
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate entry"))
                    e.printStackTrace();
            }
            ingr = getNextIngredient();
        }
    }

    private void importRecipes() {
        var rec = getNextRecipe();
        String query = "";
        while (rec != null) {
            var rec_name = rec.name.replace("\"", "\\\"");
            query = String.format("insert into recipes (name, link) values (_utf8 \"%s\", _utf8 \"%s\");",
                    rec_name,
                    rec.link);
            try {
                mysqlDataStorage.executeQuery(query);
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate entry")) {
                    e.printStackTrace();
                }
            }
            addLinkBetweenRecipeAndIngredient(rec);

            rec = getNextRecipe();
        }
    }

    private void addLinkBetweenRecipeAndIngredient(Recipe recipe){
        for (var ingr_id : recipe.ingredients){
            var ingredient_name = dataStorage.getIngredientById(ingr_id).name;
            try {
                var query = mysqlDataStorage
                        .executeQuery(String.format("select id from ingredients where name = _utf8 \"%s\"",
                                ingredient_name));
                query.next();
                var sql_ingredient_id = query.getInt("id");
                var recipe_name = recipe.name.replace("\"", "\\\"");
                query = mysqlDataStorage
                        .executeQuery(String.format("select id from recipes where name = _utf8 \"%s\"", recipe_name));
                query.next();
                var sql_recipe_id = query.getInt("id");
                if (!checkLinkBetweenRecipeIngredientInTheTable(sql_recipe_id, sql_ingredient_id))
                    mysqlDataStorage.executeQuery(String.format(
                            "insert into recipeingredients (recipe_id, ingredient_id) values (%s, %s)",
                            sql_recipe_id,
                            sql_ingredient_id));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkLinkBetweenRecipeIngredientInTheTable(int recipe_id, int ingredient_id) throws SQLException {
        var resultSet = mysqlDataStorage.executeQuery(String.format("select count(*) from recipeingredients where recipe_id = %s and ingredient_id = %s", recipe_id, ingredient_id));
        return resultSet.next() && resultSet.getInt("count(*)") > 0;
    }

    private String getNextIngredient() {
        var ingr = dataStorage.getIngredientById(currentIndex);
        if (ingr == null)
            return null;
        currentIndex++;
        return ingr.name;
    }

    private Recipe getNextRecipe() {
        var rec = dataStorage.getRecipe(recId);
        if (rec == null)
            return null;
        recId++;
        return rec;
    }

    public static void main(String [] args){
        new ImportToMySQL(new JSONDataStorage());
    }
}
