package com.ChatBot.Core;

import java.util.HashSet;
import java.util.List;

public class UserInfo {
    public final String username;
    private Context currentContext;

    private List<Recipe> dislikedFood;
    private List<Recipe> likedFood;
    private List<Recipe> history;

    public UserInfo(String username){
        this.username = username;
    }

    public void clearContext(){
        currentContext = null;
    }

    public Context getContext(){
        return currentContext;
    }

    public void initContext(){
        currentContext = new Context();
    }
}
