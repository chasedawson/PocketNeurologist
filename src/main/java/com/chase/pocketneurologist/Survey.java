package com.chase.pocketneurologist;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Chase Dawson on 8/30/2017.
 */

public class Survey extends AppCompatActivity {

    private String[] ageRanges;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ageRanges = new String[]{"younger than 30", "35 - 40", "40 - 45", "45 - 50", "50 - 55", "55 - 60", "60 - 65", "65 - 70", "70 - 75", "80 - 85", "85 - 90", "older than 90"};

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ageRanges);
        spinner.setAdapter(spinnerAdapter);


    }
}
