package com.ChatBot.Interfaces;

import com.ChatBot.Core.BotLogic;
import com.ChatBot.DataBases.JSONDataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ConsoleTests {
    private ConsoleInterface consoleInterface;

    @BeforeEach
    protected void setUp() {
        consoleInterface = new ConsoleInterface();
        consoleInterface.initialize(new BotLogic(new JSONDataStorage()));
    }

    @Test
    void removeIngredientNotThrows_WhenNoIngredients(){
        String input = "add 5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in); // A way to read custom lines
    }
}
