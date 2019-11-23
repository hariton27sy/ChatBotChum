package com.Parsers;

import com.ChatBot.Core.Recipe;
import com.ChatBot.DataBases.JSONDataStorage;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Parser {
    private int pageId;
    public boolean isWorking;

    public static void main(String[] args) {
        var pathToDataBase = "JSON";
        var db = new JSONDataStorage(pathToDataBase);
        var parser  = new Parser(263);
        var thread = new ReadingThread(parser);
        thread.start();
        while (parser.isWorking){
            var stringPage = parser.getNextPage();
            Document page = Jsoup.parse(stringPage[0]);
            Recipe recipe = new Recipe(parser.getName(page));
            var ingredients = parser.getIngredients(page);
            for (String ingr : ingredients){
                recipe.ingredients.add(db.addIngredientAndGetId(ingr));
            }
            recipe.link = stringPage[1];
            System.out.println(stringPage[1]);
            db.addRecipe(recipe);
            db.updateFiles();
        }

    }
    
    private Parser(int pageId){
        this.pageId = pageId;
        isWorking = true;
    }

    private Parser() {
        this(0);
    }

    private String[] getNextPage() {
        while (true) {
            String site = "https://www.povarenok.ru/recipes/show/";
            try {
                URL url = new URL(site + pageId + "/");

                var reader = new BufferedReader(new InputStreamReader(url.openStream(),
                        Charset.forName("windows-1251")));
                var data = new StringBuilder();
                var str = reader.readLine();

                while (str != null){
                    data.append(str);
                    str = reader.readLine();
                }

                pageId++;
                var stringData = data.toString();
                if (containsRecipe(stringData))
                    return new String[]{stringData, url.toString()};
            }
            catch (FileNotFoundException e){
                System.out.println("Page not found: " + site + pageId + "/");
                pageId++;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean containsRecipe(String page){
        return !page.contains("Запрашиваемая вами страница");
    }

    private String[] getIngredients(Document page) {
        var ingredients = page.body()
                .getElementsByClass("ingredients-bl").get(0)
                .getElementsByTag("ul").get(0).getElementsByTag("li");

        var result = new ArrayList<String>();

        for (Element ingr : ingredients) {
            result.add(ingr.getElementsByAttributeValue("itemprop", "name").get(0).text().toLowerCase());
        }

        return result.toArray(new String[0]);
    }

    private String getName(Document page){
        var name = page.body().getElementsByClass("item-bl item-about").get(0)
                .getElementsByTag("h1").get(0).text().toLowerCase();

        return name;
    }
}

class ReadingThread extends Thread {
    private Parser parser;

    public ReadingThread(Parser parser){
        this.parser = parser;
    }
    public void run(){
        Scanner scanner = new Scanner(System.in);
        while (parser.isWorking){
            var command = scanner.nextLine();
            if (command.equals("stop") || command.equals("exit"))
                parser.isWorking = false;
        }
    }
}