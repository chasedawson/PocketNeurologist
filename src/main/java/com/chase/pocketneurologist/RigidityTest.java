package com.chase.pocketneurologist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by chase on 3/8/17.
 */

public class RigidityTest extends Test {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setTestTime(10000);
        super.setTestType("Rigidity");
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
                double y = event.values[1];
                double z = event.values[2];

                /**
                 * creates single acceleration vector by squaring each acceleration of the three acceleration values, summing them together,
                 * and then taking the sqaure root of that sum
                 */
                double accel = (x * x + y * y + z * z);
                RigidityTest.super.addToRawData(Double.parseDouble(getTimeStamp()), accel);
                RigidityTest.super.addValue(Math.sqrt(accel));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        super.addSensor(sensor, sensorEventListener);
    }


    //calculates how severe the symptom is based of the most previous TestResult
    @Override
    public String getSymptomSeverity() {
        return 1 - getResult()/10 + "";
    }

}