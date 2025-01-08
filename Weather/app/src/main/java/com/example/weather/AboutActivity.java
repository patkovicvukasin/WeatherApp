package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView backButton = findViewById(R.id.backButtonA);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_ab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(AboutActivity.this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_favorites) {
                    startActivity(new Intent(AboutActivity.this, FavoritesActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    startActivity(new Intent(AboutActivity.this, CityFinder.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}

