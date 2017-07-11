package com.chase.pocketneurologist;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by chase on 2/18/17.
 */

public class Profile extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        TextView name = new TextView(this);
        name.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        name.setText("First Name");

        gridLayout.setRowCount(5);
        gridLayout.setColumnCount(2);


    }
}
