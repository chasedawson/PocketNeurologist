package com.chase.pocketneurologist;

import android.view.View;

import java.util.Date;

/**
 * Created by chase on 2/5/17.
 *
 * Purpose: Interface that defines the methods that must be implemented by a class if it is testing a symptom.
 *
 */

public interface Testable {

    public void startTesting();
    public void startCountDown(View view);
    public void endTesting();
    public void saveResult();
    public String getTestType();
    public String getTimeStamp();
    public String getSymptomSeverity();



}

//testable classes must also have a viewFlipper w/ a relativeLayout in the middle where the popup window can display the count down
