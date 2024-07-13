package com.example.previsaotempo.presenter;

import com.example.previsaotempo.model.City;
import com.example.previsaotempo.model.FavoriteCitiesManager;
import com.example.previsaotempo.view.FavoritesContract;

import java.util.List;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private FavoritesContract.View view;
    private FavoriteCitiesManager favoriteCitiesManager;

    public FavoritesPresenter(FavoritesContract.View view) {
        this.view = view;
        this.favoriteCitiesManager = FavoriteCitiesManager.getInstance();
    }

    @Override
    public void removeFromFavorites(City city) {
        favoriteCitiesManager.removeFavoriteCity(city);
        loadFavoriteCities();
    }

    @Override
    public void loadFavoriteCities() {
        List<City> favoriteCities = favoriteCitiesManager.getFavoriteCities();
        view.showFavoriteCities(favoriteCities);
    }

    @Override
    public void addToFavorites(City city) {
        favoriteCitiesManager.addFavoriteCity(city);
        loadFavoriteCities();
    }
}
