package com.ChatBot.Core;

import java.util.List;

public class UserInfo {
    public final String username;

    private List<String> dislikedFood;
    private List<String> likedFood;
    private List<String> history;

    public UserInfo(String username){
        this.username = username;
    }
}
