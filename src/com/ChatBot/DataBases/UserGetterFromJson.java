package com.ChatBot.DataBases;

import com.ChatBot.Core.UserInfo;
import org.json.*;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

class JsonUserInfoUpdater implements IUserInfoUpdater {
    String database = "Users.JSON";
    Map<String, UserInfo> users;
    public JsonUserInfoUpdater() {
    }

    @Override
    public UserInfo getUserInfo(String username) {
        if (username.equals("anonymous")){
            return new UserInfo(username);
        }

        return null;
    }

    @Override
    public void updateUserInfo(UserInfo user) {
        if (user.username.equals("anonymous")){
            return;
        }
    }
}
