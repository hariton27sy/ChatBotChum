package com.ChatBot.DataBases;

import com.ChatBot.Core.UserInfo;

public interface IUserInfoUpdater {
    public UserInfo getUserInfo(String username);
    public void updateUserInfo(UserInfo user);
}
