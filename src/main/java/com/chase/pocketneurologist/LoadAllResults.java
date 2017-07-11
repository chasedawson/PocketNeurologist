package com.chase.pocketneurologist;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by chase on 2/14/17.
 */

public class LoadAllResults extends AsyncTask<String, Void, ArrayList> {

    private Graphable caller;
    private DatabaseHandler db;

    public LoadAllResults(Graphable caller, DatabaseHandler db) {
        Log.i("LOAD", "CREATED");
        this.caller = caller;
        this.db = db;
    }


    @Override
    protected ArrayList doInBackground(String... params) {
        return db.getAllResults(params[0]); //runs query on database to get all results from one type of test
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        caller.loadGraph(arrayList);
    }
}