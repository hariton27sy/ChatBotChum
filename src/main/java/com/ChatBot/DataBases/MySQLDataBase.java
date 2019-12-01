package com.ChatBot.DataBases;

import com.ChatBot.Core.*;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.JSch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

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

    public static void main(String[] args) {
        try {
            var db = new MySQLDataBase();
            var recipe = db.getRecipe("Щука");
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ResultSet result = null;
        try {
            result = executeQuery("select * from recipes order by rand() limit 1;");
            if (result == null || !result.next())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        var ingredients = new HashSet<Integer>();


        try {
            var recipe = new Recipe(result.getString("name"));
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    @Override
    public Recipe getRecipeByRequest(String username) {
        return null;
    }

    @Override
    public Recipe getRecipeByRequest(Request request) {
        return null;
    }

    @Override
    public Recipe getRecipeByRequest(Context context) {
        return null;
    }

    @Override
    public Recipe getRecipe(String recipeName) {
        ResultSet sqlAnswer;
        try {
            var query = String.format("select * from recipes where name = _utf8 \"%s\";", recipeName);
            sqlAnswer = statement.executeQuery(query);
            var b = sqlAnswer.next();
            var recipe = new Recipe(recipeName);
            recipe.link = sqlAnswer.getString("link");
            sqlAnswer = statement.executeQuery(String.format("select ingredient_id from recipeingredients where " +
                    "recipe_id = %s", sqlAnswer.getString("id")));
            while(sqlAnswer.next()){
                recipe.ingredients.add(sqlAnswer.getInt("id"));
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
            return null;
        }

        return null;
    }

    @Override
    public UserInfo getUserInfo(String username) {
        var user = new UserInfo(username);
        user.initContext();
        var context = user.getContext();
        username = username.replace("\"", "\\\"");
        try {
            executeQuery("insert into users (name) values (username)");
        } catch (SQLException e) {
            if (!e.getMessage().contains("Duplicate entry")) {
                e.printStackTrace();
            }
        }
        var user_id = 0;
        try {
            var getUserIdQuery = executeQuery("select id from users where name = " + username);
            getUserIdQuery.next();
            user_id = getUserIdQuery.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            var ingredients_ids = executeQuery("select ingredient_id from usercontexts where user_id = " + user_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public UserInfo getUserInfo(int userId) {
        return null;
    }

    @Override
    public String[] getAllIngredients() {
        return new String[0];
    }

    @Override
    public void updateUsers(UserInfo user) { }

    @Override
    public HashSet<Recipe> getAllRecipes() {
    return null;}

    @Override
    public Ingredient getIngredientByName(String arg) {
        return null;
    }

    @Override
    public Ingredient getIngredientById(int id) {
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
}
