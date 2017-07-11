package com.chase.pocketneurologist;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by chase on 2/12/17.
 */

public class SaveResult extends AsyncTask<TestResult, Void, Void> {

    private Testable caller;
    private DatabaseHandler db;
    public SaveResult(Testable caller, DatabaseHandler db) {
        this.caller = caller;
        this.db = db;
    }

    @Override
    protected Void doInBackground(TestResult... params) {
        Log.i("PREADDED RESULT: ", params[0].toString());
        db.addResult(params[0]);
        SymptomSeverity symptomSeverity = new SymptomSeverity(new String[]{caller.getTestType(), caller.getTimeStamp(), caller.getSymptomSeverity()});
        Log.i("SR", symptomSeverity.toString());
        db.updateSymptomSeverity(symptomSeverity);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
