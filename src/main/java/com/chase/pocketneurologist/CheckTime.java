package com.chase.pocketneurologist;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by chase on 2/7/17.
 *
 * Purpose: Subclass of AsyncTask that checks the test time of the caller test to make
 * sure that the test ends after the specified time. Updates the DonutProgess on the test_in_progress.xml layout
 * every 50 milliseconds so that the user can see the progression of the test.
 *
 */

public class CheckTime extends AsyncTask<Void, Void, Void> {

    //every subclass that extends the Test class is Timeable
    private Timeable caller;

    //constructor of CheckTime, takes Timeable parameter (all tests are timeable)
    public CheckTime(Timeable caller) {
        this.caller = caller;

    }

    /**
     * section of code that runs in background
     */
    @Override
    protected Void doInBackground(Void... params) {
        //gets the test time from the class using this class
        long time = caller.getTestTime();
        try {
            while(time > 0) {
                Thread.sleep(50);
                time = time - 50;
                publishProgress();
            }
        } catch (Exception e) { Log.e("CHECK TIME", e + ""); }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //gets the test time from the class that using this class
        float time = caller.getTestTime();

        //creates a percent increment that represents the amount of time that has passed
        float progress = (50/time) * 100;

        //caller class updates the donut progress that is displaying the percentage of the test that has been completed
        caller.addProgress(progress);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        caller.endTiming();
    }
}
