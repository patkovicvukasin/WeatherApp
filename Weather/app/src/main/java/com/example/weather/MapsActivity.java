package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        if (intent != null) {
            latitude = intent.getDoubleExtra("LATITUDE", 44.7866);
            longitude = intent.getDoubleExtra("LONGITUDE", 20.4489);
        }

        ImageView backButton = findViewById(R.id.backButtonM);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String temperature = getIntent().getStringExtra("TEMPERATURE");
        String weatherIcon = getIntent().getStringExtra("WEATHER_ICON");

        WeatherFragment weatherFragment = WeatherFragment.newInstance(temperature, weatherIcon);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.weatherFragmentContainer, weatherFragment)
                .commit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_favorites) {
                    startActivity(new Intent(MapsActivity.this, FavoritesActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    startActivity(new Intent(MapsActivity.this, CityFinder.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng cityLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(cityLocation).title("Marker in Selected City"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 10));
    }


    private static String updateWIcon(int condition) {
        boolean night = isNight();

        if (condition >= 200 && condition <= 232) {
            return night ? "grmljavina_noc" : "grmljavina";
        } else if (condition >= 300 && condition <= 531) {
            return "kisa";
        } else if (condition >= 600 && condition <= 622) {
            return "sneg";
        } else if (condition >= 701 && condition <= 781) {
            return "magla";
        } else if (condition == 800) {
            return night ? "noc" : "sunce";
        } else if (condition >= 801 && condition <= 804) {
            return night ? "oblaci_noc" : "oblaci";
        } else {
            return night ? "noc" : "oblaci";
        }
    }

    private static boolean isNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 20 || hour < 6);
    }
}
