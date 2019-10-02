package com.ChatBot.DataBases;

import com.ChatBot.Core.UserInfo;

import java.util.HashSet;

public interface IUserDataLoader {
    public HashSet<UserInfo> loadUsers();
    //TODO: public void updateUserInfo(UserInfo user);
}
