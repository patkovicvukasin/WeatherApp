package com.example.weather;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteCity.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteCityDAO favoriteCityDAO();
}
