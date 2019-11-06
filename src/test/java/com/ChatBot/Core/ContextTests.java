package com.ChatBot.Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ContextTests {
    private Context userContext;

    @BeforeEach
    protected void setUp() {
        userContext = new Context();
        userContext.addIngredientAndGetRecipesCount(new Ingredient("Брюква", new HashSet<>() {{
            add(0);
            add(1);
            add(2);
        }}));
        userContext.addIngredientAndGetRecipesCount(new Ingredient("Лосось", new HashSet<>() {{
            add(0);
            add(1);
        }}));
        userContext.addIngredientAndGetRecipesCount(new Ingredient("Оливки", new HashSet<>() {{
            add(2);
        }}));
    }

    @Test
    void removeIngredientNotThrows_WhenNoIngredients(){
        Throwable thrown = catchThrowable(() -> userContext.removeIngredientAndGetRecipesCount(0));
        assertThat(thrown).isNull();
    }

    @Test
    void throwsIndexOutOfBoundException_WhenWrongIndex(){
        Throwable thrown = catchThrowable(() -> userContext.removeIngredientAndGetRecipesCount(-1));
        assertThat(thrown).isInstanceOf(IndexOutOfBoundsException.class);
        thrown = catchThrowable(() -> userContext.removeIngredientAndGetRecipesCount(150));
        assertThat(thrown).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void removesLastElementCorrectly(){
        int elemCount = userContext.removeIngredientAndGetRecipesCount(2);
        assertThat(elemCount).isEqualTo(2);
    }

    @Test
    void removesNotLastElementCorrectly(){
        int elemCount = userContext.removeIngredientAndGetRecipesCount(1);
        assertThat(elemCount).isEqualTo(1);
    }

    @Test
    void returnsZeroWhenAllIngredientsWereRemoved(){
        userContext.removeIngredientAndGetRecipesCount(2);
        userContext.removeIngredientAndGetRecipesCount(1);
        int elemCount = userContext.removeIngredientAndGetRecipesCount(0);
        assertThat(elemCount).isEqualTo(0);
    }
}
