package com.chase.pocketneurologist;

import java.util.Date;

/**
 * Created by chase on 2/12/17.
 */

public class TestResult {

    //date of test
    private String date;

    //result of test
    private String result;

    //type of test
    private String test;

    //what the test measures i.e. acceleration, gait length
    private String measure;

    public TestResult(String[] strings) {
        setTest(strings[0]);
        setMeasure(strings[1]);
        setDate(strings[2]);
        setResult(strings[3]);
    }

    public void setDate(String string) {
        date = string;
    }

    public void setResult(String string) {
        result = string;
    }

    public void setTest(String string) {
        test = string;
    }

    public void setMeasure(String string) {measure = string;}



    public String getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }

    public String getTest() {
        return test;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public String toString() {
        return getTest() + " " + getMeasure() + " " + getDate() + " " + getResult();
    }
}
