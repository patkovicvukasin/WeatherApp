package com.example.weather;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface FavoriteCityDAO {

    @Insert
    void insert(FavoriteCity city);

    @Query("SELECT * FROM favorite_cities")
    List<FavoriteCity> getAllFavoriteCities();

    @Delete
    void delete(FavoriteCity city);

    @Query("SELECT * FROM favorite_cities ORDER BY cityName ASC")
    List<FavoriteCity> getAllFavoriteCitiesSortedByName();

}
