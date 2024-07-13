package com.example.previsaotempo.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.previsaotempo.R;
import com.example.previsaotempo.model.City;
import com.example.previsaotempo.presenter.MainPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter presenter;
    private EditText cityEditText;
    private LinearLayout citiesLayout;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static final String PREFS_NAME = "CityPrefs";
    private static final String KEY_CITY_LIST = "cityList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        gson = new Gson();

        presenter = new MainPresenter(this);

        cityEditText = findViewById(R.id.cityEditText);
        citiesLayout = findViewById(R.id.citiesLayout);
        requestQueue = Volley.newRequestQueue(this);

        Button addCityButton = findViewById(R.id.addCityButton);
        Button openFavoritesButton = findViewById(R.id.openFavoritesButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button clearSearchesButton = findViewById(R.id.clearSearchesButton);

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityEditText.getText().toString().trim();
                if (!cityName.isEmpty()) {
                    presenter.addCity(cityName);
                    saveCityToList(cityName);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, insira o nome de uma cidade.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                } else {
                    redirectToLogin();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        clearSearchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedSearches();
            }
        });

        loadSavedCities();// A MainActivity é carregada sem verificar o login na inicialização
    }

    @Override
    public void addCityToView(City city) {
        View cityItemView = LayoutInflater.from(this).inflate(R.layout.city_item, null);

        TextView cityNameTextView = cityItemView.findViewById(R.id.cityNameTextView);
        TextView weatherTextView = cityItemView.findViewById(R.id.weatherTextView);
        TextView temperatureTextView = cityItemView.findViewById(R.id.temperatureTextView);
        Button addToFavoritesButton = cityItemView.findViewById(R.id.addToFavoritesButton);

        cityNameTextView.setText(city.getName());
        weatherTextView.setText(city.getWeather());
        temperatureTextView.setText("Temperatura: " + city.getTemperature() + " °C");

        addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    presenter.addToFavorites(city);
                } else {
                    redirectToLogin();
                }
            }
        });

        citiesLayout.addView(cityItemView);
    }

    @Override
    public void showCityAlreadyAddedError(String cityName) {
        Toast.makeText(this, "A cidade " + cityName + " já foi adicionada.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCityAlreadyFavoritedError(String cityName) {
        Toast.makeText(this, "A cidade " + cityName + " já está nos favoritos.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCityAddedToFavorites(String cityName) {
        Toast.makeText(this, "A cidade " + cityName + " foi adicionada aos favoritos.", Toast.LENGTH_SHORT).show();
    }
    private void saveCityToList(String cityName) {
        List<String> cityList = getSavedCityList();
        if (!cityList.contains(cityName)) {
            cityList.add(cityName);
            String jsonCities = gson.toJson(cityList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_CITY_LIST, jsonCities);
            editor.apply();
        }
    }

    private void loadSavedCities() {
        String jsonCities = sharedPreferences.getString(KEY_CITY_LIST, null);
        List<String> cityList = new ArrayList<>();
        if (jsonCities != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            cityList = gson.fromJson(jsonCities, type);
        }
        for (String city : cityList) {
            presenter.addCity(city);
        }
    }

    private List<String> getSavedCityList() {
        String jsonCities = sharedPreferences.getString(KEY_CITY_LIST, null);
        List<String> cityList = new ArrayList<>();
        if (jsonCities != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            cityList = gson.fromJson(jsonCities, type);
        }
        return cityList;
    }

    private void clearSavedSearches() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CITY_LIST); // Remove a chave das cidades pesquisadas
        editor.apply();

        // Limpa a interface do usuário
        citiesLayout.removeAllViews();
        Toast.makeText(this, "Pesquisas salvas foram removidas.", Toast.LENGTH_SHORT).show();
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void redirectToLogin() {
        Toast.makeText(this, "Por favor, faça login para continuar.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        Toast.makeText(this, "Você foi desconectado.", Toast.LENGTH_SHORT).show();
    }
}