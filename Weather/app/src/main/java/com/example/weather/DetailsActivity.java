package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsActivity extends AppCompatActivity {

    private TextView cityNameDetails;
    private TextView temperatureDetails;
    private TextView feelsLikeDetails;
    private TextView minTemperatureDetails;
    private TextView maxTemperatureDetails;
    private TextView windDetails;
    private TextView humidityDetails;
    private ImageView weatherIconDetails;
    private double latitude;
    private double longitude;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cityNameDetails = findViewById(R.id.city_name_details);
        temperatureDetails = findViewById(R.id.temperature_details);
        feelsLikeDetails = findViewById(R.id.feels_like_details);
        minTemperatureDetails = findViewById(R.id.min_temperature_details);
        maxTemperatureDetails = findViewById(R.id.max_temperature_details);
        windDetails = findViewById(R.id.wind_details);
        humidityDetails = findViewById(R.id.humidity_details);
        weatherIconDetails = findViewById(R.id.weather_icon_details);

        ImageView backButton = findViewById(R.id.backButtonD);
        ImageView appLogo = findViewById(R.id.imageView);

        String cityName = getIntent().getStringExtra("city_name");
        if (cityName != null) {
            cityNameDetails.setText(cityName + " - Vreme");
            fetchWeatherData(cityName);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        Button showMapButton = findViewById(R.id.openMaps);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(latitude, longitude);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_details);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_favorites) {
                    startActivity(new Intent(DetailsActivity.this, FavoritesActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    startActivity(new Intent(DetailsActivity.this, CityFinder.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void fetchWeatherData(String cityName) {
        WeatherService.getCurrentWeather(DetailsActivity.this, cityName, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JsonObject weatherData = new Gson().fromJson(result, JsonObject.class);
                    runOnUiThread(() -> updateUI(weatherData));

                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(DetailsActivity.this, "Greška prilikom parsiranja podataka", Toast.LENGTH_SHORT).show());
                }
            }
            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> Toast.makeText(DetailsActivity.this, "Greška prilikom dohvatanja podataka", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateUI(JsonObject weatherData) {
        if (weatherData != null) {
            JsonObject main = weatherData.getAsJsonObject("main");
            JsonObject wind = weatherData.getAsJsonObject("wind");
            JsonObject weather = weatherData.getAsJsonArray("weather").get(0).getAsJsonObject();
            JsonObject coord = weatherData.getAsJsonObject("coord");
            int weatherCondition = weather.get("id").getAsInt();

            double temp = main.get("temp").getAsDouble();
            double feelsLikeTemp = main.get("feels_like").getAsDouble();
            double minTemp = main.get("temp_min").getAsDouble();
            double maxTemp = main.get("temp_max").getAsDouble();

            latitude = coord.get("lat").getAsDouble();
            longitude = coord.get("lon").getAsDouble();

            temperatureDetails.setText(String.format("%.1f°C", temp));
            feelsLikeDetails.setText(String.format("Subjektivni osećaj: %.1f°C", feelsLikeTemp));
            minTemperatureDetails.setText(String.format("Min temperatura: %.1f°C", minTemp));
            maxTemperatureDetails.setText(String.format("Max temperatura: %.1f°C", maxTemp));
            windDetails.setText("Brzina vetra: " + wind.get("speed").getAsString() + " m/s");
            humidityDetails.setText("Vlažnost vazduha: " + main.get("humidity").getAsString() + "%");

            String iconName = updateWIcon(weatherCondition);
            int iconId = getResources().getIdentifier(iconName, "drawable", getPackageName());
            weatherIconDetails.setImageResource(iconId);
            weatherIconDetails.setTag(iconName);

        }
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

    public void openMap(double latitude, double longitude) {
        Intent intent = new Intent(DetailsActivity.this, MapsActivity.class);
        intent.putExtra("LATITUDE", latitude);
        intent.putExtra("LONGITUDE", longitude);

        String temperature = temperatureDetails.getText().toString();
        intent.putExtra("TEMPERATURE", temperature);

        String iconName = (String) weatherIconDetails.getTag();
        intent.putExtra("WEATHER_ICON", iconName);

        startActivity(intent);
    }
}
