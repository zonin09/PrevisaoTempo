package com.example.previsaotempo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("list")
    private List<CityWeather> cities;

    public List<CityWeather> getCities() {
        return cities;
    }
}

class CityWeather {
    @SerializedName("name")
    private String name;

    @SerializedName("weather")
    private List<WeatherDetails> weather;

    public String getName() {
        return name;
    }

    public List<WeatherDetails> getWeather() {
        return weather;
    }
}

class WeatherDetails {
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }
}
