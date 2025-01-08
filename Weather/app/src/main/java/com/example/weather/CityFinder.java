package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class CityFinder extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private TextView more, temperature, cityName, dateTime;

    private FavoriteCityDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);

        AppDatabase database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "favorite_cities_db").allowMainThreadQueries().build();

        dao = database.favoriteCityDAO();

        findViewById(R.id.add_to_favorites_button).setOnClickListener(v -> addCityToFavorites());
        findViewById(R.id.details_button).setOnClickListener(view -> openDetails());

        ImageView backButton = findViewById(R.id.backButton);
        SearchView search = findViewById(R.id.search_view);
        ImageView appLogo = findViewById(R.id.imageView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    fetchWeatherData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityFinder.this, AboutActivity.class);
                startActivity(intent);
            }
        });


        more = findViewById(R.id.more);
        temperature = findViewById(R.id.temperature);
        ImageView icon = findViewById(R.id.weather_icon);
        dateTime = findViewById(R.id.date_time);
        cityName = findViewById(R.id.city_name);

        String cityNameFav = getIntent().getStringExtra("city_name_fav");

        if (cityNameFav != null)
        {
            cityName.setText(cityNameFav);
            fetchWeatherData(cityNameFav);
        }
        else
        {
            cityName.setText("Novi Sad");
            fetchWeatherData("Novi Sad");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_cf);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(CityFinder.this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_favorites) {
                    startActivity(new Intent(CityFinder.this, FavoritesActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void updateDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy | HH:mm");
        String currentDateTime = dateFormat.format(new Date());
        dateTime.setText(currentDateTime);
    }

    private void fetchWeatherData(String cityName) {
        WeatherService.getCurrentWeather(CityFinder.this, cityName, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JsonObject weatherData = JsonParser.parseString(result).getAsJsonObject();
                    runOnUiThread(() -> updateUI(weatherData));

                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(CityFinder.this, "Greska u parsiranju", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> Toast.makeText(CityFinder.this, "Greska u parsiranju", Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void updateUI(JsonObject weatherData) {
        try {
            JsonObject main = weatherData.getAsJsonObject("main");
            JsonObject weather = weatherData.getAsJsonArray("weather").get(0).getAsJsonObject();

            double temp = main.get("temp").getAsDouble();
            int humidity = main.get("humidity").getAsInt();
            String newCityName = weatherData.get("name").getAsString();
            int condition = weather.get("id").getAsInt();


            cityName.setText(newCityName);
            temperature.setText(String.format("%.1f°C", temp));
            more.setText(String.format("Vlažnost: %d%%", humidity));

            String iconName = updateWIcon(condition);
            int iconId = getResources().getIdentifier(iconName, "drawable", getPackageName());

            ImageView weatherIcon = findViewById(R.id.weather_icon);
            weatherIcon.setImageResource(iconId);

            updateDateTime();

        } catch (Exception e) {
            e.printStackTrace();
            temperature.setText("Greška");
            more.setText("Greška");
        }
    }

    private static String updateWIcon(int condition) {
        boolean night = isNight();

        if (condition >= 200 && condition <= 232) {
            return night ? "grmljavina_noc" : "grmljavina";
        }
        else if (condition >= 300 && condition <= 531) {
            return "kisa";
        }
        else if (condition >= 600 && condition <= 622) {
            return "sneg";
        }
        else if (condition >= 701 && condition <= 781) {
            return "magla";
        }
        else if (condition == 800) {
            return night ? "noc" : "sunce";
        }
        else if (condition >= 801 && condition <= 804) {
            return night ? "oblaci_noc" : "oblaci";
        }
        else {
            return night ? "noc" : "oblaci";
        }
    }

    private static boolean isNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 20 || hour < 6);
    }

    private void addCityToFavorites() {
        TextView cityNameTextView = findViewById(R.id.city_name);
        String cityName = cityNameTextView.getText().toString();

        if (!isCityInFavorites(cityName))
        {
            FavoriteCity city = new FavoriteCity(cityName);
            dao.insert(city);
            Toast.makeText(this, "Grad " + cityName + " je dodat u omiljene", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Grad " + cityName + " je već u omiljenim", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCityInFavorites(String cityName) {
        List<FavoriteCity> favoriteCities = dao.getAllFavoriteCities();

        for (FavoriteCity city : favoriteCities) {
            if (city.getCityName().equalsIgnoreCase(cityName)) {
                return true;
            }
        }
        return false;
    }

    private void openDetails() {
        Intent intent = new Intent(CityFinder.this, DetailsActivity.class);
        String currentCity = cityName.getText().toString();
        intent.putExtra("city_name", currentCity);
        startActivity(intent);
    }

}