package com.ChatBot.DataBases.ParsersFromSites;

import com.ChatBot.Core.*;
import com.ChatBot.DataBases.IDataStorage;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.JSch;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class MySQLDataBase implements IDataStorage{
    private Connection connection;

    public MySQLDataBase() throws Exception{
        var properties = getPropertiesFromFile("sqlpasswords.txt");

        connectSSH(22, "breakit.ru", properties.get("sshUser"), properties.get("sshPassword"),
                3306, 4009);

        connection = connectDataBase("localhost", 4009, properties.get("dbUser"),
                properties.get("dbPassword"), "chatbotdb");

        var statement = connection.createStatement();
        var result = statement.executeQuery("show tables in chatbotdb;");
        result.next();
        System.out.println(result.getString("Tables_in_chatbotdb"));
        return;
    }

    public static void main(String[] args) {
        try {
            var db = new MySQLDataBase();
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
        return DriverManager.getConnection(url+dbName, dbUser, dbPassword);
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
        return null;
    }

    @Override
    public Recipe getRecipe(int recipeId) {
        return null;
    }

    @Override
    public UserInfo getUserInfo(String username) {
        return null;
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
    public void updateUsers(UserInfo user) {

    }

    @Override
    public HashSet<Recipe> getAllRecipes() {
        return null;
    }

    @Override
    public Ingredient getIngredientByName(String arg) {
        return null;
    }

    @Override
    public Ingredient getIngredientById(int id) {
        return null;
    }
}
