package com.example.cookit.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.cookit.Converters;
import com.example.cookit.CookIt;
import com.example.cookit.Recipe;

@Database(entities = {Recipe.class}, version = 6)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db = Room.databaseBuilder(CookIt.getContext(),
            AppLocalDbRepository.class,
            "dbFileName.db").fallbackToDestructiveMigration().build();
}
