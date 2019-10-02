package com.ChatBot.Core;

import java.util.HashSet;

public class Context {
    private HashSet<Recipe> currentSearchResult;

    public Context(HashSet<Recipe> context){
        currentSearchResult = context;
    }

    public HashSet<Recipe> getCurrentSearchResult(){
        return currentSearchResult;
    }

    public void setCurrentSearchResult(HashSet<Recipe> currentSearchResult){
        this.currentSearchResult = currentSearchResult;
    }
    //TODO: Cancel last request info
}
