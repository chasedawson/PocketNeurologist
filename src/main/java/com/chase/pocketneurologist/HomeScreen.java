package com.chase.pocketneurologist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.util.UUID;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     *
     */

    DonutProgress overallSeverityProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);




        View app_bar_home_screen = findViewById(R.id.app_bar_home_screen);
        View content_home_screen = app_bar_home_screen.findViewById(R.id.content_home_screen);


        if(!GlobalValues.getTestMode()) {
            overallSeverityProgress = (DonutProgress) content_home_screen.findViewById(R.id.donut_progress);


            overallSeverityProgress.setTextColor(getColor(R.color.colorPrimary));
            overallSeverityProgress.setTextSize(90);

            DatabaseHandler db = new DatabaseHandler(this);
           // db.deleteAll();
            ArrayList<TestResult> testResults = db.getAllResults("Tremor");
            Log.i("results", testResults.size() + "");

            ArrayList<SymptomSeverity> symptomSeverities = db.getSymptomSeverities();
            float averageSeverity = 0;
            Log.i("HOMESCREEN", symptomSeverities.size() + "");
            if (symptomSeverities.size() != 0) {
                for (int x = 0; x < symptomSeverities.size(); x++) {
                    averageSeverity += Float.parseFloat(symptomSeverities.get(x).getSeverity()) * 100;
                    Log.i("HOMESCREEN", averageSeverity + "");
                }
                averageSeverity = averageSeverity / symptomSeverities.size();
            }
            overallSeverityProgress.setProgress(averageSeverity);
            if (averageSeverity < 33.33)
                overallSeverityProgress.setFinishedStrokeColor(Color.GREEN);
            else if (averageSeverity < 66.67)
                overallSeverityProgress.setFinishedStrokeColor(Color.YELLOW);
            else
                overallSeverityProgress.setFinishedStrokeColor(Color.RED);

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(GlobalValues.getUserID() != null) {
            Log.i("USER ID", GlobalValues.getUserID());
        }

        if(!GlobalValues.getTestMode()) {
            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<SymptomSeverity> symptomSeverities = db.getSymptomSeverities();
            float averageSeverity = 0;
            Log.i("HOMESCREEN", symptomSeverities.size() + "");
            if (symptomSeverities.size() != 0) {
                for (int x = 0; x < symptomSeverities.size(); x++) {
                    averageSeverity += Float.parseFloat(symptomSeverities.get(x).getSeverity()) * 100;
                    Log.i("HOMESCREEN", averageSeverity + "");
                }
                averageSeverity = averageSeverity / symptomSeverities.size();
            }
            overallSeverityProgress.setProgress(averageSeverity);
            if (averageSeverity < 33.33)
                overallSeverityProgress.setFinishedStrokeColor(Color.GREEN);
            else if (averageSeverity < 66.67)
                overallSeverityProgress.setFinishedStrokeColor(Color.YELLOW);
            else
                overallSeverityProgress.setFinishedStrokeColor(Color.RED);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_test) {
            Intent testIntent = new Intent(this, TestList.class);
            startActivity(testIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(this, History.class);
            startActivity(historyIntent);

        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
