package com.example.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_cities")
public class FavoriteCity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String cityName;

    public FavoriteCity(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}

