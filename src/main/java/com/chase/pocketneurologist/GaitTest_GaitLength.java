package com.chase.pocketneurologist;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ViewFlipper;
import java.util.ArrayList;

/**
 * Created by chase on 3/15/17.
 *
 * Purpose: Subclass of Test that uses the step detector sensor and GPS location to estimate gait length while
 * the user walks for a period of time with the phone in their pocket.
 *
 */

public class GaitTest_GaitLength extends Test {
    private boolean testing;
    private double totalSteps;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private GetDistanceTask getDistanceTask;
    private ViewFlipper viewFlipper;
    private ArrayList<Location> locations = new ArrayList<>();
    private LocationManager locationManager;
    private String provider;
    private double totalDistance;
    private Location location;
    private Chronometer chronometer;
    private AlertDialog.Builder dialogBuilder;

    private Sensor sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setTestTime(60000);
        super.setTestType("Gait (Gait Length)");
        super.setDialogMessage("Your estimated gait length: ");
        super.setMeasure("gait length");
        super.setUnit("m");
        super.onCreate(savedInstanceState);

        getDistanceTask = new GetDistanceTask();
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        totalSteps = 0;
        sensorManager = super.getSensorManager();
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        chronometer = (Chronometer) findViewById(R.id.chronometer);


        initLocationServices();
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] == 1.0f)
                    totalSteps++;

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        super.addSensor(sensor, sensorEventListener);
    }

    @Override
    public String getSymptomSeverity() {
        return super.getResult()/10 + "";
    }

    private LocationListener locationListener = new LocationListener()  {
        @Override
        public void onLocationChanged(Location location) {
                try {
                    initLocationServices();
                    Location currentLocation = locationManager.getLastKnownLocation(provider);
                    locations.add(currentLocation);
                } catch (SecurityException e) {
                    Log.e("", e.toString());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void initLocationServices() throws SecurityException {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
    }

    private class GetDistanceTask extends AsyncTask<Void, Void, Void> {

        //runs through the ArrayList<Location> locations and calculates the totalDistance walked during the testing period
        @Override
        protected Void doInBackground(Void... params) {
            int y = 1;
            for(int x = 0; x < locations.size() - 1; x++) {
                totalDistance += locations.get(x).distanceTo(locations.get(y));
                y++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //calculates meters per step
            GaitTest_GaitLength.super.setResult(totalDistance/totalSteps);
            showDialog();
        }
    }

    @Override
    public void endTesting() {
        chronometer.stop();
        sensorManager.unregisterListener(sensorEventListener);
        getDistanceTask.execute();

    }

    public void showDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.create();
        dialogBuilder.setTitle(getDialogMessage() + totalSteps/totalDistance);
        dialogBuilder.setNegativeButton("close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        dialogBuilder.show();
        saveResult();
    }
}
