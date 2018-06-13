package com.example.cookit.Model;

public interface RecipeAsyncDaoListener<T> {
    void onComplete(T data);
}
