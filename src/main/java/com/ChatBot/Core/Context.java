package com.ChatBot.Core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

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
        if(!ingredients.contains(ingredient)){
            if (recipesIds.size() == 0){
                recipesIds.add(HashTreePSet.from(ingredient.dishesIds));
            }
            else{
                recipesIds.add(recipesIds.getLast());
                for(Integer recipe : recipesIds.get(recipesIds.size() - 2)){
                    if(!ingredient.dishesIds.contains(recipe))
                        recipesIds.set(recipesIds.size() - 1,
                                recipesIds.getLast().minus(recipe));
                }
            }
            ingredients.add(ingredient);
        }
        if(recipesIds.getFirst().size() > recipesIds.getLast().size() * 2){
            PSet<Integer> reduceSet = HashTreePSet.from(recipesIds.getLast());
            recipesIds.clear();
            recipesIds.add(reduceSet);
        }
        return recipesIds.getLast().size();
    }

    public int removeIngredientAndGetRecipesCount(int index){
        if(index >= ingredients.size()){
            throw new IndexOutOfBoundsException();
        }
        ingredients.remove(index);
        if(index == ingredients.size() - 1 && recipesIds.size() > 1){
            recipesIds.removeLast();
        }
        else if (ingredients.size() > 0){
            recipesIds.clear();
            PSet<Integer> newPSet = HashTreePSet.from(ingredients.get(0).dishesIds);
            for(int i = 1; i < ingredients.size(); i++) {
                for (Integer recipe : ingredients.get(i).dishesIds) {
                    if (!ingredients.get(i).dishesIds.contains(recipe))
                        newPSet = newPSet.minus(recipe);
                }
            }
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
        for (Ingredient ingr : ingredients){
            answer.append(ingr.name).append("\n");
        }

        return answer.toString();
    }


    public Collection<Integer> getRecipesIds(){
        return recipesIds.get(recipesIds.size() - 1);
    }
}
