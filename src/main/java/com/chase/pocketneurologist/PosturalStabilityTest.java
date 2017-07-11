package com.chase.pocketneurologist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by Chase Dawson on 6/12/2017.
 */

public class PosturalStabilityTest extends Test {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setTestTime(10000);
        super.setTestType("Tremor");
        super.setDialogMessage("Your acceleration vector: ");
        super.setMeasure("acceleration");
        super.setUnit("m/s/s");
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = super.getSensorManager();


        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                double x = event.values[0];
                double accel = (x);
                PosturalStabilityTest.super.addValue(accel);
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





}
