package com.example.cookit.Model;

import com.example.cookit.Recipe;

import java.util.List;

public interface GetAllRecipesListener {
    public void onSuccess(List<Recipe> recipeslist);
}
