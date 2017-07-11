package com.chase.pocketneurologist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by chase on 2/12/17.
 *
 * Purpose: Subclass of SQLiteOpenHelper that connects to the SQL databases and allows information form
 * tests to be saved in the form of TestResults and the severities of symptoms to be saved in the form
 * of SymptomSeverities.
 *
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //static variables
    //database version
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "testResultManager.db";

    //TestResult table
    private static final String TABLE_TEST_RESULTS = "testResults";

    //SymptomSeverity table
    private static final String TABLE_SYMPTOM_SEVERITY = "symptomSeverities";


    //testResults column names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TEST = "test";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_DATE = "date";
    private static final String KEY_RESULT = "result";

    //symptomSeverities column names
    private static final String KEY_SYMPTOM = "symptom";
    private static final String KEY_SEVERITY = "severity";
    //also has a date column but the key is the same as the one for the testResults table

    //personalInfo column names
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTH_DATE = "birthDate";


    //constructor for DatabaseHandler
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //creates both tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql code that creates testResults
        String CREATE_TEST_RESULTS_TABLE = "CREATE TABLE " + TABLE_TEST_RESULTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT," + KEY_TEST + " TEXT," + KEY_MEASURE + " TEXT," + KEY_DATE + " TEXT," + KEY_RESULT + " TEXT)";

        //sql code that creates symptomSeverities
        String CREATE_SYMPTOM_SEVERITY_TABLE = "CREATE TABLE " + TABLE_SYMPTOM_SEVERITY + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SYMPTOM +" TEXT," + KEY_DATE + " TEXT," + KEY_SEVERITY + " TEXT)";

        //executes the sql code defined in the strings above
        db.execSQL(CREATE_TEST_RESULTS_TABLE);
        db.execSQL(CREATE_SYMPTOM_SEVERITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_RESULTS);
        onCreate(db);
    }

    //adds a TestResult to testResults
    public void addResult(TestResult testResult) {

        //gets an instance of the database that can be written to
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //puts the values of the testResult into its respective column
        values.put(KEY_TEST, testResult.getTest());
        values.put(KEY_MEASURE, testResult.getMeasure());
        values.put(KEY_DATE, testResult.getDate());
        values.put(KEY_RESULT, testResult.getResult());

        //inserts the values of the testResult into testResults
        db.insert(TABLE_TEST_RESULTS, null, values);

        db.close();

    }

    //updates the a SymptomSeverity in symptomSeverities
    public void updateSymptomSeverity(SymptomSeverity symptomSeverity) {

        //gets an instance of the database that can be written to
        SQLiteDatabase db = this.getWritableDatabase();

        //removes old severity
        db.execSQL("DELETE FROM " + TABLE_SYMPTOM_SEVERITY + " WHERE " + KEY_SYMPTOM + " = " + "'" + symptomSeverity.getName() + "'");

        ContentValues values = new ContentValues();

        //puts the values of the symptomSeverity into its respective column
        values.put(KEY_SYMPTOM, symptomSeverity.getName());
        values.put(KEY_DATE, symptomSeverity.getDate());
        values.put(KEY_SEVERITY, symptomSeverity.getSeverity());

        //inserts the values of the new SymptomSeverity into symptomSeverities
        db.insert(TABLE_SYMPTOM_SEVERITY, null, values);
        db.close();


    }

    public ArrayList<SymptomSeverity> getSymptomSeverities() {
        ArrayList<SymptomSeverity> symptomSeverities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SYMPTOM_SEVERITY;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                String[] symptomSeverityConstructor = {cursor.getString(1), cursor.getString(2), cursor.getString(3)};
                SymptomSeverity symptomSeverity = new SymptomSeverity(symptomSeverityConstructor);
                symptomSeverities.add(new SymptomSeverity(symptomSeverityConstructor));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return  symptomSeverities;
    }

    //deletes all values in both tables
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEST_RESULTS);
        db.execSQL("DELETE FROM " + TABLE_SYMPTOM_SEVERITY);
    }

    //returns the TestResult from testResults that matches a specific id
    public TestResult getResult(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        //make sure data from other tests can be added
        Cursor cursor = db.query(TABLE_TEST_RESULTS, new String[]{KEY_ID,
                KEY_DATE, KEY_RESULT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        String[] resultConstructor = {cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
        TestResult testResult = new TestResult(resultConstructor);
        // return contact
        return testResult;
    }

    //returns an ArrayList<TestResult> of all the TestResults of a specific test type saved in testResults
    public ArrayList<TestResult> getAllResults(String typeOfTest) {
        ArrayList<TestResult> testResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TEST_RESULTS + " WHERE " + KEY_TEST + " = " + "'" + typeOfTest + "'";
        Log.i("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.i("SQL", cursor.getCount() + "");
        if(cursor.moveToFirst()) {
            do {
                String[] resultConstructor = {cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
                TestResult testResult = new TestResult(resultConstructor);
                testResults.add(testResult);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return testResults;
    }
}
