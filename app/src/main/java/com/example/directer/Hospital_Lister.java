package com.example.directer;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Hospital_Lister extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.hospital_screen);
        LinearLayout main_vert = findViewById(R.id.vert_layout);
        Intent display_list = getIntent();
        Bundle data = display_list.getExtras();

        String[] names_list = (String[]) data.get("Names");
        int[] travel_time = (int[]) data.get("Travels");
        int[] wait_times =  (int[]) data.get("Waits");
        int[] total_times = (int[]) data.get("Totals");
        double[] longitudes = (double[]) data.get("Longitudes");
        double[] latitudes = (double[]) data.get("Latitudes");

        for (int i = 0; i < names_list.length; i++) {
            LinearLayout new_hospital = new LinearLayout(Hospital_Lister.this);
            new_hospital.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
            new_hospital.setOrientation(LinearLayout.VERTICAL);
            new_hospital.setId(i);
            main_vert.addView(new_hospital);
            TextView name = new TextView(this);
            name.setText(names_list[i] + " ");
            name.setHeight(150);
            name.setHorizontallyScrolling(false);
            name.setTextSize(15);
            name.setGravity(Gravity.LEFT);
            name.setPadding(0, 0, 0, 25);
            new_hospital.addView(name);
            LinearLayout times = new LinearLayout(Hospital_Lister.this);
            times.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
            times.setOrientation(LinearLayout.HORIZONTAL);
            times.setPadding(0, 0, 0, 25);
            TextView travel = new TextView(this);
            travel.setText("Travel time: " + String.valueOf(travel_time[i]) + " min ");
            travel.setTextSize(10);
            travel.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1));
            travel.setGravity(Gravity.CENTER_HORIZONTAL);
            times.addView(travel);
            TextView wait = new TextView(this);
            wait.setText("Wait time: " + String.valueOf(wait_times[i]) + " min ");
            wait.setTextSize(10);
            wait.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1));
            wait.setGravity(Gravity.CENTER_HORIZONTAL);
            times.addView(wait);
            TextView total = new TextView(this);
            total.setText("Total time: " + String.valueOf(total_times[i]) + " min ");
            total.setTextSize(10);
            total.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1));
            total.setGravity(Gravity.CENTER_HORIZONTAL);
            times.addView(total);
            new_hospital.addView(times);
            new_hospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitudes[v.getId()] + "," + longitudes[v.getId()]);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    System.exit(0);
                }
            });

        }
    }
}
