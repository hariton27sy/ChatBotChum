package com.ChatBot.Core;

import java.util.ArrayList;

public class Request {
    public ArrayList<Integer> ingredientIds;

    public Request(Iterable<Integer> iterable){
        ingredientIds = new ArrayList<>();
        iterable.iterator().forEachRemaining(ingredientIds::add);
    }
}
