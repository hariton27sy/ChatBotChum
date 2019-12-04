package com.ChatBot.Core;

import java.util.*;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

public class Context {
    private ArrayList<Ingredient> ingredients;
    private LinkedList<PSet<Integer>> recipesIds;

    public Context(){
        ingredients = new ArrayList<>();
        recipesIds = new LinkedList<>();
    }

    public int addIngredientAndGetRecipesCount(Ingredient ingredient) {
        if(!containsIngredient(ingredient)){
            if (recipesIds.size() == 0)
                recipesIds.add(HashTreePSet.from(ingredient.dishesIds));
            else
                recipesIds.add(retainIntegersIn(recipesIds.getLast(), ingredient.dishesIds));
            ingredients.add(ingredient);
        }

        //Reduce amount of allocated memory
        if(recipesIds.getFirst().size() > recipesIds.getLast().size() * 2){
            PSet<Integer> reduceSet = HashTreePSet.from(recipesIds.getLast());
            recipesIds.clear();
            recipesIds.add(reduceSet);
        }
        return recipesIds.getLast().size();
    }

    private PSet<Integer> retainIntegersIn(PSet<Integer> firstSet, Set<Integer> secondSet){
        PSet<Integer> retainResult = firstSet;
        for(Integer integer : firstSet){
            if(!secondSet.contains(integer))
                retainResult = retainResult.minus(integer);
        }
        return retainResult;
    }

    public int removeIngredientAndGetRecipesCount(String name){
        int counter = 0;
        for(Ingredient ingredient : ingredients){
            if (ingredient.name.equalsIgnoreCase(name)){
                return removeIngredientAndGetRecipesCount(counter);
            }
            counter++;
        }
        throw new IndexOutOfBoundsException();
    }

    public int removeIngredientAndGetRecipesCount(int index){
        if(index >= ingredients.size() || index < 0){
            throw new IndexOutOfBoundsException();
        }

        ingredients.remove(index);
        if(index == ingredients.size() && recipesIds.size() > 1)
            recipesIds.removeLast();
        else if (ingredients.size() > 0){
            //Recalculate set intersection
            recipesIds.clear();
            PSet<Integer> newPSet = HashTreePSet.from(ingredients.get(0).dishesIds);
            for(int i = 1; i < ingredients.size(); i++)
                newPSet = retainIntegersIn(newPSet, ingredients.get(i).dishesIds);
            recipesIds.add(newPSet);
        }
        else{
            recipesIds.clear();
            return 0;
        }
        return recipesIds.getLast().size();
    }

    public String ingredientsListToString(){
        if (ingredients == null)
            return "";
        var answer = new StringBuilder();
        int counter = 1;
        for (Ingredient ingr : ingredients){
            answer.append(counter++)
                    .append(". ")
                    .append(ingr.name)
                    .append("\n");
        }
        return answer.toString();
    }

    public Collection<String> getIngredientsAsStringCollection(){
        ArrayList<String> result = new ArrayList<>();
        for(Ingredient ingr : ingredients)
            result.add(ingr.name);
        return result;
    }

    public Collection<Integer> getRecipesIds(){
        if(recipesIds.size() == 0)
            return new ArrayList<>();
        return recipesIds.get(recipesIds.size() - 1);
    }

    public int getRecipesCount(){
        return recipesIds.getLast().size();
    }

    private boolean containsIngredient(Ingredient ingredient){
        for(var ingr : ingredients){
            if (ingr.name.equals(ingredient.name))
                return true;
        }
        return false;
    }
}
