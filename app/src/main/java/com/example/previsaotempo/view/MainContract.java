package com.example.previsaotempo.view;

import com.example.previsaotempo.model.City;

public interface MainContract {
    interface View {
        void addCityToView(City city);
        void showCityAlreadyAddedError(String cityName);
        void showCityAlreadyFavoritedError(String cityName); // Novo método
        void showCityAddedToFavorites(String cityName); // Novo método
    }

    interface Presenter {
        void addCity(String cityName);
        void addToFavorites(City city);
    }
}
