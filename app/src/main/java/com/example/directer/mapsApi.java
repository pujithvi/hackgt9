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


public class mapsApi extends AppCompatActivity {

    private List<Map.Entry<String, ArrayList<Integer>>> list;
    public Object doInBackground (Object[] params) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0e_AjT0xXkIjPu3VswknGPL62DVSp4oI")
                .build();
        LatLng current_loc = new LatLng(MapsActivity.latitude, MapsActivity.longitude);
        System.out.println("Lat: " + current_loc.lat + " Long: " + current_loc.lng);
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
        HashMap<String, ArrayList<Integer>> times_map = new HashMap<String, ArrayList<Integer>>();
        System.out.println("Places: " + places.size());
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
                        int waitTime = (int) (Math.random() * 25) + 1;
                        ArrayList<Integer> times = new ArrayList<>();
                        times.add(travelTime);
                        times.add(waitTime);
                        times.add(travelTime + waitTime);
                        times_map.put(p.name, times);
                    }
                }
                System.out.println("Finished place: " + p.name);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Distance Matrix Calculation Failed.");
            }


            //times_map.put(p.name, )
        }
        List<Map.Entry<String, ArrayList<Integer>>> list = new LinkedList<>(times_map.entrySet());
        Collections.sort(list, (l1, l2) -> l1.getValue().get(2).compareTo(l2.getValue().get(2)));
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        context.shutdown();
        Intent display_list = new Intent(this, Hospital_Lister.class);
        String[] names_list = new String[list.size()];
        int[] travel_time = new int[list.size()];
        int[] wait_time = new int[list.size()];
        int[] total_time = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            names_list[i] = list.get(i).getKey();
            travel_time[i] = list.get(i).getValue().get(0);
            wait_time[i] = list.get(i).getValue().get(1);
            total_time[i] = list.get(i).getValue().get(2);
        }
        display_list.putExtra("Names", names_list);
        display_list.putExtra("Travels", travel_time);
        display_list.putExtra("Waits", wait_time);
        display_list.putExtra("Totals", total_time);
// Invoke .shutdown() after your application is done making requests
        startActivity(display_list);
        return null;
    }
    public void onPostExecute(Object[] params) {
        doInBackground(params);
    }

    public void onCreate() {

    }

}