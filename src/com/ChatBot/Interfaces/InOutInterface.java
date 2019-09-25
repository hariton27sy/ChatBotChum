package com.ChatBot.Interfaces;

import com.ChatBot.Core.UserInfo;

public interface InOutInterface {
    String receive();

    void send(String answer);

    void greetUser(UserInfo user);

    UserInfo initializeNewUser();
}
