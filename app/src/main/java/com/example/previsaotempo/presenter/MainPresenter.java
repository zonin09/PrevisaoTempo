package com.example.previsaotempo.presenter;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.previsaotempo.model.City;
import com.example.previsaotempo.model.FavoriteCitiesManager;
import com.example.previsaotempo.view.MainActivity;
import com.example.previsaotempo.view.MainContract;

import java.util.HashSet;
import java.util.Set;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private RequestQueue requestQueue;
    private String apiKey = "79d1fa5660d29b47c84ed1da1eb785c9";
    private Set<String> addedCities;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        this.requestQueue = Volley.newRequestQueue((MainActivity) view);
        this.addedCities = new HashSet<>(); // Inicializar o Set
    }

    @Override
    public void addCity(String cityName) {
        if (addedCities.contains(cityName.toLowerCase())) {
            // Cidade já foi adicionada, não adicionar novamente
            view.showCityAlreadyAddedError(cityName);
            return;
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String cityName = response.getString("name");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String weatherDesc = weatherObj.getString("description");
                            String temperature = response.getJSONObject("main").getString("temp");

                            City city = new City(cityName, weatherDesc, temperature);

                            addedCities.add(cityName.toLowerCase()); // Adicionar a cidade ao Set
                            view.addCityToView(city);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void addToFavorites(City city) {
        if (FavoriteCitiesManager.getInstance().isCityFavorite(city)) {
            view.showCityAlreadyFavoritedError(city.getName());
        } else {
            FavoriteCitiesManager.getInstance().addFavoriteCity(city);
            view.showCityAddedToFavorites(city.getName());
        }
    }
}
