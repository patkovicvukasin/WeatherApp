package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class FavoriteCityAdapter extends RecyclerView.Adapter<FavoriteCityAdapter.FavoriteCityViewHolder> {

    private List<FavoriteCity> favoriteCities;
    private Context context;
    private FavoriteCityDAO dao;

    public FavoriteCityAdapter(Context context, List<FavoriteCity> favoriteCities, FavoriteCityDAO dao) {
        this.context = context;
        this.favoriteCities = favoriteCities;
        this.dao = dao;
    }

    @NonNull
    @Override
    public FavoriteCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_city_item, parent, false);
        return new FavoriteCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteCityViewHolder holder, int position) {
        FavoriteCity city = favoriteCities.get(position);
        holder.cityNameFav.setText(city.getCityName());

        fetchWeather(holder, city.getCityName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CityFinder.class);
                intent.putExtra("city_name_fav", city.getCityName());
                v.getContext().startActivity(intent);
            }
        });

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_favorite_city, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.remove_from_favorites) {
                        removeCityFromFavorites(city);
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteCities.size();
    }

    public static class FavoriteCityViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameFav;
        ImageView icon;
        TextView temp;
        ImageView options;

        public FavoriteCityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityNameFav = itemView.findViewById(R.id.city_name_fav);
            icon = itemView.findViewById(R.id.icon_fav);
            temp = itemView.findViewById(R.id.temp_fav);
            options = itemView.findViewById(R.id.moreOptions);
        }
    }

    private void fetchWeather(FavoriteCityViewHolder holder, String cityName) {
        WeatherService.getCurrentWeather(holder.itemView.getContext(), cityName, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    WeatherData weatherData = WeatherData.fromJson(jsonObject);

                    if (weatherData != null) {
                        ((AppCompatActivity) context).runOnUiThread(() -> {
                            holder.temp.setText(String.format(Locale.getDefault(), "%s°C", weatherData.getTemperature()));

                            int iconId = holder.itemView.getContext().getResources().getIdentifier(
                                    weatherData.getwIcon(), "drawable", holder.itemView.getContext().getPackageName());
                            holder.icon.setImageResource(iconId);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                ((AppCompatActivity) context).runOnUiThread(() -> {
                    Toast.makeText(holder.itemView.getContext(), "Greška prilikom dohvatanja podataka", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    private void removeCityFromFavorites(FavoriteCity city) {
        new Thread(() -> {
            dao.delete(city);
            favoriteCities.remove(city);
            ((AppCompatActivity)context).runOnUiThread(this::notifyDataSetChanged);
        }).start();
    }

    public void updateData(List<FavoriteCity> favoriteCities) {
        this.favoriteCities.clear();
        this.favoriteCities.addAll(favoriteCities);
        notifyDataSetChanged();
    }



}

