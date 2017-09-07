package com.chase.pocketneurologist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Chase Dawson on 8/30/2017.
 */

public class Survey extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String[] questions;
    private String[][] spinners;
    private int currentQuestion = 0;
    private TextView question;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] selectedItems;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        spinners = new String[7][];
        selectedItems = new String[7];

        //7 questions
        questions = new String[]{"What is your approximate age range?", "What is your sex?", "Bradykinesia?", "Postural Instability?", "Rigidity?", "Tremor?", "Levodopa?"};
        spinners[0] = new String[]{"younger than 30", "35 - 40", "40 - 45", "45 - 50", "50 - 55", "55 - 60", "60 - 65", "65 - 70", "70 - 75", "80 - 85", "85 - 90", "older than 90"};
        spinners[1] = new String[]{"male", "female"};
        for (int x = 2; x < questions.length; x++) {
            spinners[x] = new String[]{"yes", "no"};
        }

        question = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        question.setText(questions[currentQuestion]);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinners[currentQuestion]);
        spinner.setAdapter(adapter);

    }

    public void nextQuestion(View v) {
        currentQuestion++;
        if (currentQuestion == questions.length) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.addPersonalInfo(selectedItems);
            this.finish();
        }
        else {
            question.setText(questions[currentQuestion]);
            adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinners[currentQuestion]);
            spinner.setAdapter(adapter);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedItems[currentQuestion] = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}
