package com.ChatBot.DataBases.JSONSerializers;

import com.ChatBot.Core.Recipe;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecipeDeserialize implements JsonDeserializer<Recipe> {

    @Override
    public Recipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        var name = object.get("name");
        var tags = this.<String>getArray(object, "tags");
        var ingredients = this.<Integer>getArray(object, "ingredients");

        return null;
    }

    public <T> ArrayList<T> getArray(JsonObject object, String member){
        var result = new ArrayList<T>();
        var temp = object.getAsJsonArray(member);
        for (JsonElement e : temp){
            var gson = new Gson();
            var type = new TypeToken<T>(){}.getType();
            result.add(gson.fromJson(e, type));
        }
        return result;
    }
}
