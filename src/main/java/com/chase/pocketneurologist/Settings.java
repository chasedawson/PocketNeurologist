package com.chase.pocketneurologist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.UUID;

/**
 * Created by Chase Dawson on 6/28/2017.
 */

public class Settings extends AppCompatActivity {

    ToggleButton toggleButton;
    Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleButton = (ToggleButton) findViewById(R.id.testModeToggle);
        toggleButton.setChecked(GlobalValues.getTestMode());

        button = (Button)findViewById(R.id.db_button);
        button.setText("View Databases");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent dbmanager = new Intent(Settings.this, AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButton.setChecked(GlobalValues.getTestMode());
    }

    public void toggleTestMode(View v) {
        GlobalValues.setTestMode(!GlobalValues.getTestMode());
        Toast.makeText(Settings.this, "" + GlobalValues.getTestMode(), Toast.LENGTH_LONG);
    }

    public static void generateNewUserId(View v) {
        GlobalValues.generateNewUserId();
    }

    public void exportResults(View v) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.exportDatabase("rawDataTestResults", "raw_data_test_results.csv");
        db.exportDatabase("testResults", "test_results.csv");
        db.exportDatabase("personalInfo", "personalInfo.csv");
    }
}
