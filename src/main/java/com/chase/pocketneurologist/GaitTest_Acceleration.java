package com.chase.pocketneurologist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by chase on 3/15/17.
 *
 * Purpose: Subclass of Test that uses the accelerometer sensor to record data while the user walks
 * for a certain period of time with the phone in their pocket. Trends in the data can be used to predict
 * the severity of bradykinesia.
 *
 */



public class GaitTest_Acceleration extends Test {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        /**
         * Every subclass of Test must first define the test length, test type, message that will be displayed through an AlertDialog when the test is over,
         * the type of data recorded by the test, and the unit for the type of data.
         */
        super.setTestTime(10000);
        super.setTestType("Gait");
        super.setDialogMessage("Your acceleration vector: ");
        super.setMeasure("acceleration");
        super.setUnit("m/s/s");
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = super.getSensorManager();


        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                //acceleration in the x-axis
                double x = event.values[0];

                //acceleration in the y-axis
                double y = event.values[1];

                //acceleration in the z-axis
                double z = event.values[2];

                //combines the accelerations from all three axises into one acceleration vector
                double accel = (x * x + y * y + z * z);

                GaitTest_Acceleration.super.addToRawData(Double.parseDouble(getTimeStamp()),x,y,z);

                //adds this calculated acceleration vector to an arrayList in the
                GaitTest_Acceleration.super.addValue(Math.sqrt(accel));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        super.addSensor(sensor, sensorEventListener);
    }


    //uses the result calculated from this test to determine the current severity of the symptom tested
    @Override
    public String getSymptomSeverity() {
        return super.getResult()/10 + "";
    }
}
