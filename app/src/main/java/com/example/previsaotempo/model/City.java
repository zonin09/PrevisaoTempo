package com.example.previsaotempo.model;

import java.io.Serializable;

public class City implements Serializable {
    private String name;
    private String weather;
    private String temperature;

    public City(String name, String weather, String temperature) {
        this.name = name;
        this.weather = weather;
        this.temperature = temperature;
    }

    // Getter para name
    public String getName() {
        return name;
    }

    // Setter para name
    public void setName(String name) {
        this.name = name;
    }

    // Getter para weather
    public String getWeather() {
        return weather;
    }

    // Setter para weather
    public void setWeather(String weather) {
        this.weather = weather;
    }

    // Getter para temperature
    public String getTemperature() {
        return temperature;
    }

    // Setter para temperature
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
