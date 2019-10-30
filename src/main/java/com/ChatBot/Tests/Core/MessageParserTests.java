package com.ChatBot.Tests.Core;

import com.ChatBot.Core.Message;
import com.ChatBot.Core.Commands;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

public class MessageParserTests {

    @Test
    void DefaultMessageParse(){
        Message message = new Message("покажи");
        Assert.assertEquals(Commands.Show, message.command);
        message = new Message("найди");
        Assert.assertEquals(Commands.Find, message.command);
        message = new Message("добавь");
        Assert.assertEquals(Commands.Add, message.command);
    }

    @Test
    void FunctionalMessageParse(){
        Message message = new Message("ПокАжи блюдо");
        Assert.assertEquals(Commands.Show, message.command);
        Assert.assertEquals(1, message.args.length);
    }
}
