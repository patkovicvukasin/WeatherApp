package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favorites;
    private FavoriteCityAdapter adapter;
    private FavoriteCityDAO dao;

    private final AtomicBoolean isSortedAlphabetically = new AtomicBoolean(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ImageView backButton = findViewById(R.id.backButtonF);
        ImageView appLogo = findViewById(R.id.imageView);
        ImageView refreshButton = findViewById(R.id.refresh_button);
        ImageView sortButton = findViewById(R.id.sort_button);

        AppDatabase database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "favorite_cities_db").allowMainThreadQueries().build();

        dao = database.favoriteCityDAO();

        favorites = findViewById(R.id.favorites_recycler_view);
        favorites.setLayoutManager(new LinearLayoutManager(this));

        loadFavoriteCities();

        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFavoriteCities();
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean currentSortState = isSortedAlphabetically.get();
                isSortedAlphabetically.set(!currentSortState);

                if (isSortedAlphabetically.get()) {
                    List<FavoriteCity> favoriteCities = dao.getAllFavoriteCitiesSortedByName();
                    Toast.makeText(FavoritesActivity.this, "Sortirano po abecedi", Toast.LENGTH_SHORT).show();
                    adapter.updateData(favoriteCities);
                } else {
                    loadFavoriteCities();
                    Toast.makeText(FavoritesActivity.this, "Prikaz po defaultu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_fav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(FavoritesActivity.this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    startActivity(new Intent(FavoritesActivity.this, CityFinder.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void loadFavoriteCities(){
        List<FavoriteCity> favoriteCities = dao.getAllFavoriteCities();
        adapter = new FavoriteCityAdapter(this, favoriteCities, dao);
        favorites.setAdapter(adapter);
    }


}
