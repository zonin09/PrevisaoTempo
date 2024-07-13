package com.example.previsaotempo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.previsaotempo.R;
import com.example.previsaotempo.model.City;
import com.example.previsaotempo.presenter.FavoritesPresenter;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoritesContract.View {
    private FavoritesPresenter presenter;
    private LinearLayout favoritesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        presenter = new FavoritesPresenter(this); // Passa o contexto da activity para o presenter
        favoritesLayout = findViewById(R.id.favoritesLayout);

        presenter.loadFavoriteCities();
    }

    @Override
    public void addToFavoritesView(City city) {
        // Inflar o layout customizado do item
        View favoriteItemView = LayoutInflater.from(this).inflate(R.layout.favorite_item, favoritesLayout, false);

        // Referenciar os elementos de UI do item
        TextView cityNameTextView = favoriteItemView.findViewById(R.id.favoriteCityNameTextView);
        TextView weatherTextView = favoriteItemView.findViewById(R.id.favoriteWeatherTextView);
        TextView temperatureTextView = favoriteItemView.findViewById(R.id.favoriteTemperatureTextView);
        Button removeFromFavoritesButton = favoriteItemView.findViewById(R.id.removeFromFavoritesButton);

        // Configurar os valores dos TextViews
        cityNameTextView.setText(city.getName());
        weatherTextView.setText(city.getWeather());
        temperatureTextView.setText("Temperatura: " + city.getTemperature() + " °C");

        // Configurar o botão de remoção
        removeFromFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.removeFromFavorites(city);
                favoritesLayout.removeView(favoriteItemView);  // Remove a view da cidade favorita
            }
        });

        // Adicionar o item à lista de favoritos
        favoritesLayout.addView(favoriteItemView);
    }

    @Override
    public void showFavoriteCities(List<City> favoriteCities) {
        favoritesLayout.removeAllViews(); // Limpa a lista antes de adicionar os favoritos

        for (City city : favoriteCities) {
            addToFavoritesView(city);
        }
    }
}
