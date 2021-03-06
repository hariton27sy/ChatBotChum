package com.ChatBot.Core;

import com.ChatBot.DataBases.JSONDataStorage;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class BotLogicTests {
    private IBotLogic botLogic;

    @BeforeEach
    protected void setUp() {
        botLogic = new BotLogic(new JSONDataStorage());
    }

    @Test
    void analyzeAndGetAnswerHelp() throws Exception {
        Message parsedMessage = new Message("/help");
        String actualAnswer = botLogic.analyzeAndGetAnswer("Oleg", parsedMessage);
        String expectedAnswer = "1. Покажи рецепт : завершает поиск или показывает случайный рецепт.\n" +
                "2. Добавь <название ингредиента> : добавляет ингредиент в запрос.\n" +
                "3. Добавлено : показывает добавленные в запрос ингредиенты.\n" +
                "4. Очисти запрос: очищает список добавленных ингредиентов\n" +
                "5. Удали <индекс/название ингредиента> : удаляет ингредиент из запроса";
        Assert.assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void analyzeAndGetAnswerRandomAnswer() throws Exception {
        for(int i = 0; i < 100; i++) {
            Message parsedMessage = new Message("покажи блюдо");
            String actualAnswer = botLogic.analyzeAndGetAnswer("Oleg", parsedMessage);
            Assert.assertTrue(actualAnswer.length() > 0);
        }
    }

    @Test
    void formRequestAndGetAnswerTest() throws Exception {
        JSONDataStorage database = new JSONDataStorage();
        for(int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * database.getAllIngredients().length);
            Message parsedMessage = new Message("добавь " + database.getAllIngredients()[random]);
            botLogic.analyzeAndGetAnswer("Oleg", parsedMessage);
            parsedMessage = new Message("покажи блюдо");
            String answer = botLogic.analyzeAndGetAnswer("Oleg", parsedMessage);
            Assert.assertNotNull(answer);
            Assert.assertTrue(answer.length() > 0);
        }
    }
}