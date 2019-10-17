package com.ChatBot.Tests.Core;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.Message;
import com.ChatBot.Core.UserInfo;
import com.ChatBot.Core.commands;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class MessageParserTests {

    @Test
    void DefaultMessageParse(){
        Message message = new Message("покажи");
        Assert.assertEquals(commands.Show, message.command);
        message = new Message("найди");
        Assert.assertEquals(commands.Find, message.command);
        message = new Message("добавь");
        Assert.assertEquals(commands.Add, message.command);
    }

    @Test
    void FunctionalMessageParse(){
        Message message = new Message("Покажи блюдо");
        Assert.assertEquals(commands.Show, message.command);
        Assert.assertEquals(1, message.args.length);
    }
}
