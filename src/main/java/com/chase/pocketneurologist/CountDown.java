package com.chase.pocketneurologist;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by chase on 2/5/17.
 *
 * Purpose: Subclass of AsyncTask that displays the count down before each
 * test begins. Uses popup windows to show the count down. Lasting 5 seconds, the count
 * down gives the user time to quickly prepare for the test.
 *
 */

public class CountDown extends AsyncTask<Void, Integer, Void> {

    //test that calls this class
    private Testable caller;

    //the popup window that is displayed
    private PopupWindow counterPopupWindow;

    //text view on the relative layout that holds the number of seconds left until the test begins
    private TextView counterPopupText;

    //relative layout on which the popup window holding the count down is displayed
    private RelativeLayout middleRelativeLayout;

    private final String TAG = "CountDown";



    public CountDown(Testable caller, RelativeLayout layout, PopupWindow popupWindow, TextView textView) {
        this.caller = caller;

        //layout that the CounterPopupWindow is displayed on
        middleRelativeLayout = layout;

        //PopupWindow that has a text view that displays the counterPopupText
        counterPopupWindow = popupWindow;

        //TextView that displays how many seconds left until testing beings
        counterPopupText = textView;

    }
    @Override
    protected Void doInBackground(Void... params) {
        //starts at 4 since the counterPopupText is already set to 5
        int x = 4;

        //thread sleeps for a total of 5 seconds and the counterPopupText is updated every second
        while (x >= 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.d(TAG, e.toString());}
            publishProgress(x);
            x--;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        counterPopupWindow.dismiss();

        //sets counterPopupText to the number of seconds left until testing
        counterPopupText.setText(values[0] +"");

        //counterPopupText is displayed as long as the as the seconds left doesn't equal 0
        if(values[0] != 0)
            counterPopupWindow.showAtLocation(middleRelativeLayout, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        //when the count down has ended, testing begins
        caller.startTesting();
    }
}
