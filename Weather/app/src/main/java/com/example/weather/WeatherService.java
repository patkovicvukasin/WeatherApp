package com.example.weather;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WeatherService {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String UNIT = "&units=metric&lang=eng";

    public static void getCurrentWeather(Context context, String cityName, final WeatherCallback callback) {
        String urlString = BASE_URL + cityName + "&appid=" + API_KEY + UNIT;

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Successful response
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error in response
                        callback.onFailure("Error: " + error.toString());
                    }
                });

        queue.add(stringRequest);
    }

    public interface WeatherCallback {
        void onSuccess(String result);
        void onFailure(String error);
    }
}
