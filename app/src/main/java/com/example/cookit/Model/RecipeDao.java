package com.example.cookit.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.cookit.Recipe;
import com.example.cookit.User;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("select * from Recipe")
    List<Recipe> getAll();

    @Query("select * from Recipe where id = :recipeId")
    Recipe getRecipeById(String recipeId);


    @Query("delete from Recipe where id = :recipeId")
    void deleteRecipeById(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);
}
