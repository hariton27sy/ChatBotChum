package com.ChatBot.Tests.Core;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.Core.Message;
import com.ChatBot.Core.UserInfo;
import com.ChatBot.DataBases.JSONDataStorage;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


class BotLogicTests {

    @Test
    void analyzeAndGetAnswerHelp() throws Exception {
        UserInfo user = new UserInfo("Oleg");
        Message parsedMessage = new Message("/help");
        String actualAnswer = BotLogic.analyzeAndGetAnswer(user, parsedMessage);
        String expectedAnswer = "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                "2. Найди рецепт : формирует запрос на поиск рецепта. (Не работает)\n" +
                "3. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n" +
                "4. Ингредиенты : показывает все ингредиенты, которые я знаю.\n" +
                "5. Добавлено : показывает добавленные в запрос ингредиенты.\n" +
                "6. Очисти запрос: очищает список добавленных ингредиентов\n" +
                "7. Выйди / выйти: выходит из программы.";
        Assert.assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void analyzeAndGetAnswerRandomAnswer() throws Exception {
        UserInfo user = new UserInfo("Oleg");
        for(int i = 0; i < 100; i++) {
            Message parsedMessage = new Message("покажи блюдо");
            String actualAnswer = BotLogic.analyzeAndGetAnswer(user, parsedMessage);
            Assert.assertTrue(actualAnswer.length() > 0);
        }
    }

    @Test
    void formRequestAndGetAnswerTest() throws Exception {
        UserInfo user = new UserInfo("Oleg");
        JSONDataStorage database = new JSONDataStorage();
        for(int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * database.getAllIngredients().length);
            Message parsedMessage = new Message("добавь " + database.getAllIngredients()[random]);
            BotLogic.analyzeAndGetAnswer(user, parsedMessage);
            parsedMessage = new Message("покажи блюдо");
            String answer = BotLogic.analyzeAndGetAnswer(user, parsedMessage);
            Assert.assertNotNull(answer);
            Assert.assertTrue(answer.length() > 0);
        }
    }
}