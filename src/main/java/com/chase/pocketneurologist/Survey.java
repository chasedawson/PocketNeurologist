package com.chase.pocketneurologist;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.Transition;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Chase Dawson on 8/30/2017.
 */

public class Survey extends AppCompatActivity {

    private String[] ageRanges;
    private String[] sex;
    private ViewGroup rootScene;
    private Scene firstScene;
    private Scene secondScene;
    //private Spinner[] spinners;
    private ArrayAdapter<String>[] adapters;
    private String[] questions;
    private String[][] spinners;
    private int currentQuestion = 0;
    private TextView question;
    private Spinner spinner;
    private Button button;
    private ArrayAdapter<String> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        spinners = new String[7][];

        //7 questions
        questions = new String[]{"What is your approximate age range?", "What is your sex?", "Bradykinesia?", "Postural Instability?", "Rigidity?", "Tremor?", "Levodopa?"};
        spinners[0] = new String[]{"younger than 30", "35 - 40", "40 - 45", "45 - 50", "50 - 55", "55 - 60", "60 - 65", "65 - 70", "70 - 75", "80 - 85", "85 - 90", "older than 90"};
        spinners[1] = new String[]{"male", "female"};
        for (int x = 2; x < questions.length; x++) {
            spinners[x] = new String[]{"yes", "no"};
        }

        question = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.button);

        question.setText(questions[currentQuestion]);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinners[currentQuestion]);
        spinner.setAdapter(adapter);



        /*
        rootScene = (ViewGroup) findViewById(R.id.root_scene);
        firstScene = Scene.getSceneForLayout(rootScene, R.layout.survey_age_range, this);
        secondScene = Scene.getSceneForLayout(rootScene, R.layout.survey_sex, this);

        spinners = new Spinner[5];
        adapters = new ArrayAdapter[5];

        //first Screen of Survey
        ageRanges = new String[]{"younger than 30", "35 - 40", "40 - 45", "45 - 50", "50 - 55", "55 - 60", "60 - 65", "65 - 70", "70 - 75", "80 - 85", "85 - 90", "older than 90"};
        spinners[0] = (Spinner) findViewById(R.id.spinner);
        adapters[0] = new ArrayAdapter<String>(this, R.layout.spinner_item, ageRanges);
        spinners[0].setAdapter(adapters[0]);


        sex = new String[]{"male", "female"};
        adapters[1] = new ArrayAdapter<String>(this, R.layout.spinner_item, sex);*/

    }

    public void nextQuestion(View v) {
        currentQuestion++;
        question.setText(questions[currentQuestion]);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinners[currentQuestion]);
        spinner.setAdapter(adapter);
    }
}
