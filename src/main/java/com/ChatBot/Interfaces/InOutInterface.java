package com.ChatBot.Interfaces;

import com.ChatBot.Core.UserInfo;

public interface InOutInterface {
    String receive() throws Exception;

    void send(String answer) throws Exception;
}
