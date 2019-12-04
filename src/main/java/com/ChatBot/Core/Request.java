package com.ChatBot.Core;

import java.util.ArrayList;
import java.util.Collection;

public class Request {
    public ArrayList<Integer> ingredientIds;

    public Request(Collection<Integer> iterable){
        ingredientIds = new ArrayList<>();
        iterable.iterator().forEachRemaining(ingredientIds::add);
    }
}
