package com.chase.pocketneurologist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by chase on 3/8/17.
 */

public class TremorTest extends Test {
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
                double y = event.values[1];
                double z = event.values[2];
                double accel = (x * x + y * y + z * z);

                TremorTest.super.addToRawData(Double.parseDouble(getTimeStamp()), x, y, z);
                TremorTest.super.addValue(Math.sqrt(accel));
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