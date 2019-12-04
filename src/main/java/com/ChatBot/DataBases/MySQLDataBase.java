package com.ChatBot.DataBases;

import com.ChatBot.Core.*;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.JSch;
import org.jsoup.helper.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class MySQLDataBase implements IDataStorage{
    private Connection dbConnection;
    private Session sshSession;
    private Statement statement;

    public MySQLDataBase() throws Exception{
        var properties = getPropertiesFromFile("sqlpasswords.txt");

        sshSession = connectSSH(22, "breakit.ru", properties.get("sshUser"), properties.get("sshPassword"),
                3306, 4009);

        dbConnection = connectDataBase("localhost", 4009, properties.get("dbUser"),
                properties.get("dbPassword"), "chatbotdb");

        statement = dbConnection.createStatement();
        statement.executeQuery("set character set 'utf8'");
    }

    private Session connectSSH(int port, String host, String sshUser, String sshPassword, int dbPort, int localDbPort)
            throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, host, port);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        int assignedPort = session.setPortForwardingL(localDbPort, "localhost", dbPort);
        return session;
    }

    private Connection connectDataBase(String host, int port, String dbUser, String dbPassword, String dbName)
            throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + host +":" + port + "/";
        Class.forName(driver);
        var props = new Properties();
        props.put("user", dbUser);
        props.put("password", dbPassword);
        props.put("useUnicode", "true");
        props.put("characterEncoding", "utf8");
        return DriverManager.getConnection(url+dbName + "?useSSL=false", props);
    }

    private HashMap<String, String> getPropertiesFromFile(String filename) throws IOException {
        var file = new BufferedReader(new FileReader(filename));
        var result = new HashMap<String, String>();
        var temp = "";
        while ((temp = file.readLine()) != null){
            var field = temp.split(":");
            result.put(field[0], field[1]);
        }

        return result;
    }

    @Override
    public Recipe getRandomRecipe() {
        ResultSet query = null;
        try {
            query = executeQuery("select id from recipes order by rand() limit 1;");
            if (query != null && query.next())
                return getRecipe(query.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Recipe getRecipeByRequest(String username) {
        return null;
    }

    @Override
    public Recipe getRecipeByRequest(Request request) {
        var sql_request = new StringBuilder("select * from (select count(*) as count, recipe_id from ");
        sql_request.append("recipeingredients where ").append("ingredient_id = ")
                .append(StringUtil.join(request.ingredientIds, " or ingredient_id = "))
                .append(" group by recipe_id) as t2 where t2.count = ")
                .append(request.ingredientIds.size());


        try {
            var result = executeQuery(sql_request.toString());
            if (result != null && result.next())
                return getRecipe(result.getInt("recipe_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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
        ResultSet sqlAnswer;
        try {
            recipeName = recipeName.replace("\"", "\\\"");
            var query = String.format("select * from recipes where name = _utf8 \"%s\";", recipeName);
            sqlAnswer = statement.executeQuery(query);
            var b = sqlAnswer.next();
            var recipe = new Recipe(recipeName);
            recipe.link = sqlAnswer.getString("link");
            sqlAnswer = statement.executeQuery(String.format("select ingredient_id from recipeingredients where " +
                    "recipe_id = %s", sqlAnswer.getInt("id")));
            while(sqlAnswer.next()){
                recipe.ingredients.add(sqlAnswer.getInt("ingredient_id"));
            }

            return recipe;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public Recipe getRecipe(int recipeId) {
        ResultSet request = null;
        try {
            request = executeQuery("select name from recipes where id = " + recipeId);
            if (request.next())
                return getRecipe(request.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public UserInfo getUserInfo(String username) {
        var user = new UserInfo(username);
        username = "\"" + username.replace("\"", "\\\"") + "\"";
        try {
            executeQuery(String.format("insert into users (name) values (_utf8 %s)", username));
        } catch (SQLException e) {
            if (!e.getMessage().contains("Duplicate entry")) {
                e.printStackTrace();
            }
        }
        var user_id = 0;
        try {
            var getUserIdQuery = executeQuery("select id from users where name = _utf8 " + username);
            getUserIdQuery.next();
            user_id = getUserIdQuery.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            var ingredients_ids = executeQuery("select ingredient_id from usercontexts where user_id = "
                    + user_id);
            user.initContext();
            var context = user.getContext();
            boolean isThereIngredients = false;
            var ingredients = new ArrayList<Integer>();
            while (ingredients_ids != null && ingredients_ids.next()){
                isThereIngredients = true;
                ingredients.add(ingredients_ids.getInt("ingredient_id"));
            }

            for (var id : ingredients){
                context.addIngredientAndGetRecipesCount(getIngredientById(id));
            }

            if (isThereIngredients)
                return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        user.clearContext();
        return user;
    }

    @Override
    public UserInfo getUserInfo(int userId) {
        return null;
    }

    @Override
    public String[] getAllIngredients() {
        ArrayList<String> resultList = new ArrayList<>();

        ResultSet answer = null;
        try {
            answer = executeQuery("select name from ingredients");
            while (answer != null && answer.next()){
                resultList.add(answer.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList.toArray(new String[0]);
    }

    @Override
    public int getIngredientId(String ingredient) {
        try {
            ResultSet answer = executeQuery(String.format("select id from ingredients where name = _utf8 \"%s\"",
                    ingredient));

            if (answer != null && answer.next())
                return answer.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int[] getAllIngredientsIds() {
        ArrayList<Integer> ingredientsIds = new ArrayList<>();
        try {
            ResultSet answer = executeQuery("select id from ingredients");
            while (answer != null && answer.next())
                ingredientsIds.add(answer.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toIntArray(ingredientsIds);
    }

    @Override
    public void updateUser(UserInfo user) {
        var username = user.username.replace("\"", "\\\"");
        try {
            executeQuery(String.format("delete from usercontexts where (select id from users where " +
                    "name = _utf8 \"%s\");", username));
            if (user.getContext() == null) {
                return;
            }
            Collection<String> ingredients = user.getContext().getIngredientsAsStringCollection();

            for (var ingr : ingredients){
                if (!isThereRowInUserContexts(user.username, ingr)){
                    var query = String.format("insert into usercontexts (user_id, ingredient_id) values ((select id from " +
                                    "users where name = _utf8 \"%s\"), (select id from ingredients where name = _utf8 \"%s\"));",
                            user.username, ingr);
                    executeQuery(query);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashSet<Recipe> getAllRecipes() {
    return null;}

    @Override
    public Ingredient getIngredientByName(String arg) {
        ResultSet result;
        try {
            result = executeQuery(String.format("select * from ingredients where name = _utf8 \"%s\"", arg));
            if (result != null && result.next()){
                var ingredient = new Ingredient(arg);
                result = executeQuery("select recipe_id from recipeingredients where ingredient_id = " +
                        result.getInt("id"));
                while (result.next())
                    ingredient.addDish(result.getInt("recipe_id"));
                return ingredient;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Ingredient getIngredientById(int id) {
        try{
            var result = executeQuery("select name from ingredients where id = " + id);
            if (result != null && result.next())
                return getIngredientByName(result.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close(){
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sshSession.disconnect();
    }

    public ResultSet executeQuery(String query) throws SQLException {
    if (statement.execute(query))
        return statement.getResultSet();
    return null;
    }

    private boolean isThereRowInUserContexts(String username, String ingredientName) throws SQLException {
        var query = String.format("select count(*) as count from " +
                        "usercontexts where user_id = (select id from users " +
                        "where name = _utf8 \"%s\") and ingredient_id = " +
                        "(select id from ingredients where name = _utf8 \"%s\");",
                username, ingredientName);

        var result = executeQuery(query);
        return result.next() && result.getInt("count") > 0;
    }

    private int[] toIntArray(Collection<Integer> collection){
        int[] result = new int[collection.size()];
        int counter = 0;
        for (Integer element : collection)
            result[counter++] = element;
        return result;
    }
}
