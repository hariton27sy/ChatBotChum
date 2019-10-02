package com.ChatBot.Interfaces;

import com.ChatBot.Core.UserInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TelegramInterface implements InOutInterface{
    public String receive() {
        throw new NotImplementedException();
    }

    public void send(String answer) {
        throw new NotImplementedException();
    }

}
