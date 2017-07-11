package com.chase.pocketneurologist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Chase Dawson on 6/28/2017.
 */

public class Settings extends AppCompatActivity {

    ToggleButton toggleButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleButton = (ToggleButton) findViewById(R.id.testModeToggle);

        toggleButton.setChecked(Mode.getTestMode());



    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButton.setChecked(Mode.getTestMode());
    }

    public void toggleTestMode(View v) {
        Mode.setTestMode(!Mode.getTestMode());
        Toast.makeText(Settings.this, "" + Mode.getTestMode(), Toast.LENGTH_LONG);
    }
}
