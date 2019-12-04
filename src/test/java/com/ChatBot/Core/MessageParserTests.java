package com.ChatBot.Core;

import org.junit.Assert;

import org.junit.jupiter.api.Test;

public class MessageParserTests {

    @Test
    void defaultMessageParse(){
        Message message = new Message("покажи");
        Assert.assertEquals(Commands.SHOW, message.command);
        message = new Message("найди");
        Assert.assertEquals(Commands.FIND, message.command);
        message = new Message("добавь");
        Assert.assertEquals(Commands.ADD, message.command);
    }

    @Test
    void functionalMessageParse(){
        Message message = new Message("ПокАжи блюдо");
        Assert.assertEquals(Commands.SHOW, message.command);
        Assert.assertEquals(1, message.args.length);
    }
}
