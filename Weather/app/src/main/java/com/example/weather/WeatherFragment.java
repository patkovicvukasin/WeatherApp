package com.example.weather;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class WeatherFragment extends Fragment {

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(String temperature, String iconName) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("temperature", temperature);
        args.putString("weather_icon", iconName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        Bundle args = getArguments();
        if (args != null) {
            String temperature = args.getString("temperature");
            String iconName = args.getString("weather_icon");

            TextView temperatureTW = view.findViewById(R.id.temperatureFrag);
            temperatureTW.setText(temperature);

            ImageView weatherIcon = view.findViewById(R.id.iconFrag);
            int iconId = getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
            weatherIcon.setImageResource(iconId);
        }
        return view;
    }
}
