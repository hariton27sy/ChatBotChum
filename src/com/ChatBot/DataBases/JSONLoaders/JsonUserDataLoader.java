package com.ChatBot.DataBases.JSONLoaders;

import com.ChatBot.Core.UserInfo;
import com.ChatBot.DataBases.IUserDataLoader;
import com.ChatBot.DataBases.UserDataStorage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;

public class JsonUserDataLoader implements IUserDataLoader {
    @Override
    public HashSet<UserInfo> loadUsers() {
        throw new NotImplementedException();
    }
}
