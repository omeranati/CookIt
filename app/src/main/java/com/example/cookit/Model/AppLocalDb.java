package com.example.cookit.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.cookit.CookIt;
import com.example.cookit.Recipe;

@Database(entities = {Recipe.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db = Room.databaseBuilder(CookIt.context,
            AppLocalDbRepository.class,
            "dbFileName.db").fallbackToDestructiveMigration().build();
}
