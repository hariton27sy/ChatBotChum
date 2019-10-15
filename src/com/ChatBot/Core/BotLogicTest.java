package com.ChatBot.Core;

import jdk.jshell.spi.ExecutionControl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotLogicTest {

    //@Test
    void analyzeAndGetAnswerHelp() throws ExecutionControl.NotImplementedException {
        UserInfo user = new UserInfo("Oleg");
        Message parsedMessage = new Message("/help");
        String actualAnswer = BotLogic.analyzeAndGetAnswer(user, parsedMessage);
        String expectedAnswer = "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                "2. Найди рецепт : формирует запрос на поиск рецепта.\n" +
                "3. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n";
        Assert.assertEquals(expectedAnswer, actualAnswer);
    }
}