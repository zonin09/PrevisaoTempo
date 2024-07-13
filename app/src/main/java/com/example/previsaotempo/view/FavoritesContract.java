package com.example.previsaotempo.view;

import com.example.previsaotempo.model.City;
import java.util.List;

public interface FavoritesContract {

    interface View {
        void addToFavoritesView(City city);
        void showFavoriteCities(List<City> favoriteCities);
    }

    interface Presenter {
        void loadFavoriteCities();
        void addToFavorites(City city);
        void removeFromFavorites(City city);
    }
}