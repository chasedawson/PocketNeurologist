package com.chase.pocketneurologist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.MatrixCursor;

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

    //RawData table
    private static final String TABLE_RAW_DATA_TEST_RESULTS = "rawDataTestResults";

    //SymptomSeverity table
    private static final String TABLE_SYMPTOM_SEVERITY = "symptomSeverities";


    //testResults column names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TEST_ID ="test_id";
    private static final String KEY_TEST = "test";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_DATE = "date";
    private static final String KEY_RESULT = "result";

    /*rawDataTestResults column names
     *
     *
     * raw_datum should be in the form of "x_value y_value z_value timestamp"
     */
    private static final String KEY_TIME_STAMP = "time";
    private static final String KEY_X_AXIS = "x_value";
    private static final String KEY_Y_AXIS = "y_value";
    private static final String KEY_Z_AXIS = "z_value";

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

        String databasePath = context.getDatabasePath("testResultManager.db").getPath();

        Log.d("DB", databasePath);

    }


    //creates both tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql code that creates testResults
        String CREATE_TEST_RESULTS_TABLE = "CREATE TABLE " + TABLE_TEST_RESULTS + " ("
                + KEY_USER_ID + " TEXT," + KEY_TEST_ID + " TEXT," + KEY_TEST + " TEXT," + KEY_MEASURE + " TEXT," + KEY_DATE + " TEXT," + KEY_RESULT + " TEXT)";


        String CREATE_RAW_DATA_TEST_RESULTS_TABLE = "CREATE TABLE " + TABLE_RAW_DATA_TEST_RESULTS + " ("
                + KEY_USER_ID + " TEXT," + KEY_TEST_ID + " TEXT," + KEY_TIME_STAMP + " TEXT," + KEY_X_AXIS + " TEXT," + KEY_Y_AXIS + " TEXT," + KEY_Z_AXIS + " TEXT)";

        //sql code that creates symptomSeverities
        String CREATE_SYMPTOM_SEVERITY_TABLE = "CREATE TABLE " + TABLE_SYMPTOM_SEVERITY + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SYMPTOM +" TEXT," + KEY_DATE + " TEXT," + KEY_SEVERITY + " TEXT)";

        //executes the sql code defined in the strings above
        db.execSQL(CREATE_TEST_RESULTS_TABLE);
        db.execSQL(CREATE_SYMPTOM_SEVERITY_TABLE);
        db.execSQL(CREATE_RAW_DATA_TEST_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAW_DATA_TEST_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYMPTOM_SEVERITY);
        onCreate(db);
    }

    //adds a TestResult to testResults
    public void addResult(TestResult testResult) {

        //gets an instance of the database that can be written to
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //puts the values of the testResult into its respective column
        values.put(KEY_USER_ID, GlobalValues.getUserID());
        values.put(KEY_TEST_ID, GlobalValues.getTestID());
        values.put(KEY_TEST, testResult.getTest());
        values.put(KEY_MEASURE, testResult.getMeasure());
        values.put(KEY_DATE, testResult.getDate());
        values.put(KEY_RESULT, testResult.getResult());

        //inserts the values of the testResult into testResults
        db.insert(TABLE_TEST_RESULTS, null, values);
        Log.i("info", "yay we got here");
        db.close();

    }

    public void addRawData(Double[] rawData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, GlobalValues.getUserID());
        values.put(KEY_TEST_ID, GlobalValues.getTestID());
        values.put(KEY_TIME_STAMP, rawData[0] + "");
        values.put(KEY_X_AXIS, rawData[1] + "");
        values.put(KEY_Y_AXIS, rawData[2] + "");
        values.put(KEY_Z_AXIS, rawData[3] + "");

        db.insert(TABLE_RAW_DATA_TEST_RESULTS, null, values);

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
        db.execSQL("DELETE FROM " + TABLE_RAW_DATA_TEST_RESULTS);
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

    ///////////////
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}
