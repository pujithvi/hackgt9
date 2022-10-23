package com.example.directer;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;

public class mapsApi extends AsyncTask {
    public Object doInBackground (Object[] objects) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0e_AjT0xXkIjPu3VswknGPL62DVSp4oI")
                .build();
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(context,
                    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(results[0].addressComponents));

// Invoke .shutdown() after your application is done making requests
        context.shutdown();
        return null;
    }
    public void onPostExecute() {

    }


}