package com.ChatBot.DataBases;

import com.ChatBot.Core.UserInfo;

import java.util.HashMap;
import java.util.HashSet;

public class UserDataStorage {
    private HashMap<String, UserInfo> users;

    public UserDataStorage(HashSet<UserInfo> users){
        this.users = new HashMap<>();
        for(UserInfo user : users){
            this.users.put(user.username, user);
        }
    }

    public HashMap<String, UserInfo> getAllUsers(){
        return users;
    }
}
