package com.example.directer;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Hospital_Lister extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        LinearLayout main_vert = findViewById(R.id.vert_layout);
        Intent display_list = getIntent();
        Bundle data = display_list.getExtras();
        String[] names_list = (String[]) data.get("Names");
        int[] travel_time = (int[]) data.get("Travels");
        int[] wait_times =  (int[]) data.get("Waits");
        int[] total_times = (int[]) data.get("Totals");
        for (int i = 0; i < names_list.length; i++) {
            LinearLayout new_hospital = new LinearLayout(this);
            new_hospital.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
            new_hospital.setOrientation(LinearLayout.HORIZONTAL);
            main_vert.addView(new_hospital);
            TextView name = new TextView(this);
            name.setText(names_list[i]);
            new_hospital.addView(name);
            TextView travel = new TextView(this);
            travel.setText(travel_time[i]);
            new_hospital.addView(travel);
            TextView wait = new TextView(this);
            wait.setText(wait_times[i]);
            new_hospital.addView(wait);
            TextView total = new TextView(this);
            total.setText(total_times[i]);
            new_hospital.addView(total);

        }
        ScrollView scroller = findViewById(R.id.hospitals_list);
        setContentView(scroller);
    }
}
