package com.chase.pocketneurologist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import java.util.UUID;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by chase on 3/8/17.
 */

public class Test extends AppCompatActivity implements Testable, Timeable{


    private String test_type = "";
    private LayoutInflater layoutInflater;
    private RelativeLayout middleRelativeLayout;
    private View counterPopup;
    private PopupWindow counterPopupWindow;
    private TextView counterPopupText;
    private CountDown countDownTask;
    private CheckTime checkTimeTask;
    private SaveResult saveResultTask;
    private SensorManager sensorManager;
    private ArrayList<Sensor> sensors = new ArrayList<>();
    private ArrayList<SensorEventListener> sensorEventListeners = new ArrayList<>();
    private ViewFlipper viewFlipper;
    private AlertDialog.Builder dialogBuilder;
    private ArrayList<Double> values;
    private Double result;
    private long test_time;
    private String dialog_message;
    private DonutProgress donutProgress;
    private Chronometer chronometer;
    private String measure;
    private String unit;
    private ArrayList<Double[]> rawData = new ArrayList<>();
    private String testID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_in_progress);

        TextView title = (TextView) findViewById(R.id.test_title);
        title.setText(getTestType() + " Test");

        testID = UUID.randomUUID().toString();
        GlobalValues.setTestID(testID);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        middleRelativeLayout = (RelativeLayout) findViewById(R.id.middle_rl);
        counterPopup = layoutInflater.inflate(R.layout.counter_popup, null);
        counterPopupWindow = new PopupWindow(counterPopup, ViewFlipper.LayoutParams.WRAP_CONTENT, ViewFlipper.LayoutParams.WRAP_CONTENT);
        counterPopupText = (TextView) counterPopup.findViewById(R.id.count5);
        DatabaseHandler db = new DatabaseHandler(this);

        countDownTask = new CountDown(this, middleRelativeLayout, counterPopupWindow, counterPopupText);
        checkTimeTask = new CheckTime(this);
        saveResultTask = new SaveResult(this, db);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setFinishedStrokeColor(getColor(R.color.colorAccent));
        donutProgress.setTextColor(getColor(R.color.colorPrimary));
        donutProgress.setProgress(0);

        test_time = getTestTime();
        values = new ArrayList<>();


    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTask.cancel(true);
        checkTimeTask.cancel(true);
        saveResultTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void startCountDown(View view) {
        viewFlipper.showNext();
        counterPopupWindow.showAtLocation(middleRelativeLayout, Gravity.CENTER, 0, 0);
        countDownTask.execute();
    }

    @Override
    public void startTesting() {
        for(int x = 0; x < sensors.size(); x++) {
            sensorManager.registerListener(sensorEventListeners.get(x), sensors.get(x), SensorManager.SENSOR_DELAY_NORMAL);
        }
        viewFlipper.showNext();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        checkTimeTask.execute();
    }


    @Override
    public void endTesting() {
        for(int x = 0; x < sensorEventListeners.size(); x++) {
            sensorManager.unregisterListener(sensorEventListeners.get(x));
        }
        chronometer.stop();
        result = getAverage();
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.create();

        if(!GlobalValues.getTestMode()) {
            DecimalFormat decimalFormat = new DecimalFormat("####.####");
            dialogBuilder.setTitle(getDialogMessage() + decimalFormat.format(result) + " " + unit);
        }
        else
            dialogBuilder.setTitle("This test is complete! Thanks!");

        dialogBuilder.setNegativeButton("close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);
                    }
                });
        dialogBuilder.show();

        saveResult();

        String formattedRawData = "";
        DecimalFormat format = new DecimalFormat("####.####");
        for (int x = 0; x < rawData.size(); x++) {
            String str = "\n" + format.format(rawData.get(x)[0]) + " " + format.format(rawData.get(x)[1])
            + " " + format.format(rawData.get(x)[2]) + " " + format.format(rawData.get(x)[3]);
            formattedRawData = formattedRawData + str;
        }


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pocketneurologist@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "New Test Data for " + getTestType());
        i.putExtra(Intent.EXTRA_TEXT   , "Average Value: "  + getResult() + " m/s/s\nRaw Data: " + formattedRawData);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Test.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void endTiming() {
        endTesting();
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public void saveResult() {
        String[] resultConstructor = {getTestType(), measure, getTimeStamp(), result + ""};
        TestResult testResult = new TestResult(resultConstructor);
        saveResultTask.execute(testResult);
    }

    @Override
    public String getTestType() {
        return test_type;
    }

    public void setTestType(String type) {
        test_type = type;
    }

    @Override
    public long getTestTime() {
        return test_time;
    }

    public void setTestTime(long time) {
        test_time = time;
    }

    @Override
    public String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() + "";    }

    @Override
    public void addProgress(float addedProgress) {
        donutProgress.setProgress(donutProgress.getProgress() + addedProgress);
    }

    public void addSensor(Sensor sensor, SensorEventListener sensorEventListener) {
        sensors.add(sensor);
        sensorEventListeners.add(sensorEventListener);
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void addValue(double value) {
        values.add(value);
    }

    public String getDialogMessage(){
        return dialog_message;
    }

    public void setDialogMessage(String message) {
        dialog_message = message;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setUnit(String unit) {this.unit = unit;}

    public Double getAverage() {
        double sum = 0;
        for(double d : values) {
            sum += d;
        }
        double average = sum/values.size();
        return average;
    }

    public void addToRawData(Double timestamp, Double x, Double y, Double z) {
        Double[] rawDatum = {timestamp, x, y, z};
        rawData.add(rawDatum);
    }

    public ArrayList<Double[]> getRawData() {
        return rawData;
    }


    //subclass must override
    @Override
    public String getSymptomSeverity() {
        String severity = result/10 + "";
        return severity;
    }



}
