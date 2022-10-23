package com.example.directer;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.TravelMode;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.KeyException;
import java.util.*;


public class mapsApi extends AppCompatActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }
    public List<Map.Entry<String, ArrayList<Object>>> doInBackground (Object[] params) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0e_AjT0xXkIjPu3VswknGPL62DVSp4oI")
                .build();
        LatLng current_loc = new LatLng(MapsActivity.latitude, MapsActivity.longitude);
        Integer radius = ((Integer) params[2]) * 1600;
        ArrayList<PlacesSearchResult> places = new ArrayList<PlacesSearchResult>();
        NearbySearchRequest search_results = PlacesApi.nearbySearchQuery(context, current_loc).radius(radius);
        search_results.type(PlaceType.HOSPITAL);
        try {
            PlacesSearchResponse result = search_results.await();
            Collections.addAll(places, result.results);
        } catch (Exception e) {
            System.out.println("Search Request Failed.");
        }
        HashMap<String, ArrayList<Object>> times_map = new HashMap<String, ArrayList<Object>>();
        for (PlacesSearchResult p : places) {
            try{
                boolean openNow = p.openingHours.openNow;
                if (!openNow) continue;
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }

            LatLng location = p.geometry.location;
            DistanceMatrixApiRequest req = new DistanceMatrixApiRequest(context).origins(current_loc).destinations(location).mode(TravelMode.DRIVING);
            try {
                DistanceMatrix dist = req.await();
                for (DistanceMatrixRow row : dist.rows) {
                    for (DistanceMatrixElement ele : row.elements) {
                        System.out.println(ele.duration.humanReadable);
                        int travelTime = Integer.parseInt(ele.duration.humanReadable.split(" ")[0]);
                        int waitTime = (int) (Math.random() * 30) + 20;
                        ArrayList<Object> data = new ArrayList<>();
                        data.add(travelTime);
                        data.add(waitTime);
                        data.add(travelTime + waitTime);
                        data.add(location);
                        times_map.put(p.name, data);
                    }
                }
                System.out.println("Finished place: " + p.name);
            } catch (Exception e) {
                System.out.println(e);

            }


            //times_map.put(p.name, )
        }
        List<Map.Entry<String, ArrayList<Object>>> list = new LinkedList<>(times_map.entrySet());
        Collections.sort(list, Comparator.comparing(l -> ((Integer) l.getValue().get(2))));
        /**for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }*/
        context.shutdown();
        return list;
    }
    public List<Map.Entry<String, ArrayList<Object>>> onPostExecute(Object[] params) {
        return doInBackground(params);
    }

}