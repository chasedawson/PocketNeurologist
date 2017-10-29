package com.chase.pocketneurologist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by chase on 3/14/17.
 */

public class TestResultsActivity extends AppCompatActivity implements Graphable {

    private String testResultType;
    private GraphView graph;
    private TextView avgRateOfChange;
    private LoadAllResults loadAllResultsTask;
    private DatabaseHandler db;
    private ArrayList<TestResult> testResults;
    private AlertDialog.Builder dialogBuilder;
    private String graphTitle;
    private String unit;
    private ImageView arrow;
    private double avgRate;
    private double criticalValue;
    private String sign;

    /*
     *  subclasses must setGraphTitle, setTestResultType, and setUnit before the super.onCreate() method
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_results);

        graph = (GraphView) findViewById(R.id.graph);
        avgRateOfChange = (TextView) findViewById(R.id.avgRateOfChange);

        arrow = (ImageView) findViewById(R.id.arrow);

        db = new DatabaseHandler(this);
        loadAllResultsTask = new LoadAllResults(this, db);
        if(testResultType != null)
            loadAllResultsTask.execute(testResultType);
        else
            Log.e("TestResults", "testResultType is null");
    }

    @Override
    public void loadGraph(ArrayList<TestResult> arrayList) {
        testResults = arrayList;
        avgRate = calculateAverageRateOfChange();

        DataPoint[] dataPoints = new DataPoint[testResults.size()];
        for(int x = 0; x < testResults.size(); x++) {
            double result = Double.parseDouble(testResults.get(x).getResult());
            dataPoints[x] = new DataPoint(x + 1, result);


        }
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dataPoints);
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph.getViewport().setScalableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScrollable(true);
        graph.setTitle(graphTitle);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                TestResult clickedTestResult = testResults.get((int)dataPoint.getX() - 1);
                Log.i("GOT HERE", "henlo");
                Log.i("TEST RESULT!", clickedTestResult.toString());
                displayMoreInfo(clickedTestResult);
            }
        });


        DecimalFormat decimalFormat = new DecimalFormat("####.####");
        avgRateOfChange.setText("Average Rate of Change: " + decimalFormat.format(avgRate));
        setArrowDirection();
        if(avgRate != 0)
            setArrowColor();


    }

    @Override
    public void displayMoreInfo(TestResult testResult) {
        TestResult clickedTestResult = testResult;

        DecimalFormat dc = new DecimalFormat("####.####");
        double value = Double.parseDouble(clickedTestResult.getResult());


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(clickedTestResult.getDate()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Toast.makeText(this, "Date: " + month + "/" + day + "/" + year + " TestResult: " + dc.format(value) + " " + unit, Toast.LENGTH_LONG).show();
    }

    public double getAverageRateOfChange() {
        return avgRate;
    }

    public double calculateAverageRateOfChange() {
        double changeInX = testResults.size();
        double firstResult = Double.parseDouble(testResults.get(0).getResult());
        double lastResult = Double.parseDouble(testResults.get(testResults.size() - 1).getResult());
        double rate = (lastResult - firstResult)/changeInX;
        return rate;
    }

    @Override
    public DatabaseHandler getDatabase() {
        return db;
    }

    public void setTestResultType(String type) {
        testResultType = type;
    }

    public String getTestResultType() {
        return testResultType;
    }

    public void setGraphTitle(String title) {
        graphTitle = title;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }


    public void rotateArrowUp() {
        RotateAnimation anim = new RotateAnimation(0f, -30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000);
        anim.setFillAfter(true);

        arrow.setAnimation(anim);
        arrow.animate();
    }

    public void rotateArrowDown() {
        RotateAnimation anim = new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000);
        anim.setFillAfter(true);

        arrow.setAnimation(anim);
        arrow.animate();
    }

    public void setArrowDirection() {
        if(avgRate < 0) {
            rotateArrowDown();
        }
        else if(avgRate > 0) {
            rotateArrowUp();
        }
    }
    public void setArrowColorGreenParameters(double criticalValue, String sign) {
        this.criticalValue = criticalValue;
        this.sign = sign;
    }

    public void setArrowColor() {
        if(sign.equals("<")) {
            if(avgRate < criticalValue) {
                arrow.setImageResource(R.mipmap.greenarrow);
            }
            else
                arrow.setImageResource(R.mipmap.redarrow);
        }

        else if(sign.equals(">")) {
            if(avgRate > criticalValue) {
                arrow.setImageResource(R.mipmap.greenarrow);
            }
            else
                arrow.setImageResource(R.mipmap.redarrow);
        }
    }

}
