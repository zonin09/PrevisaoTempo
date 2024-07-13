package com.example.previsaotempo.model;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCitiesManager {
    private static FavoriteCitiesManager instance;
    private List<City> favoriteCities;

    private FavoriteCitiesManager() {
        favoriteCities = new ArrayList<>();
    }

    public static FavoriteCitiesManager getInstance() {
        if (instance == null) {
            instance = new FavoriteCitiesManager();
        }
        return instance;
    }

    public void addFavoriteCity(City city) {
        if (!isCityFavorite(city)) {
            favoriteCities.add(city);
        }
    }

    public void removeFavoriteCity(City city) {
        favoriteCities.remove(city);
    }

    public List<City> getFavoriteCities() {
        return new ArrayList<>(favoriteCities);
    }

    public boolean isCityFavorite(City city) {
        for (City favoriteCity : favoriteCities) {
            if (favoriteCity.getName().equalsIgnoreCase(city.getName())) {
                return true;
            }
        }
        return false;
    }
}
