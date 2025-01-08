package com.example.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class WeatherData {

    private String temperature, wIcon, city, weatherType;
    private int Condition;
    
    public static WeatherData fromJson(JSONObject jsonObject){
        try{
            WeatherData weatherData = new WeatherData();
            weatherData.city = jsonObject.getString("name");
            weatherData.Condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherData.wIcon = updateWIcon(weatherData.Condition);
            double temp = jsonObject.getJSONObject("main").getDouble("temp");
            int value = (int) Math.rint(temp);
            weatherData.temperature = Integer.toString(value);
            return weatherData;
            
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWIcon(int condition) {
        boolean night = isNight();

        if(condition >= 200 && condition <= 232) {
            return night ? "grmljavina_noc" : "grmljavina";
        }
        else if(condition >= 300 && condition <= 531) {
            return "kisa";
        }
        else if(condition >= 600 && condition <= 622) {
            return "sneg";
        }
        else if(condition >= 701 && condition <= 781) {
            return "magla";
        }
        else if(condition == 800) {
            return night ? "noc" : "sunce";
        }
        else if(condition >= 801 && condition <= 804) {
            return night ? "oblaci_noc" : "oblaci";
        }
        else {
            return night ? "oblaci_noc" : "oblaci";
        }
    }

    private static boolean isNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 20 || hour < 6);
    }

    public String getTemperature() {
        return temperature;
    }

    public String getwIcon() {
        return wIcon;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherType() {
        return weatherType;
    }

}
