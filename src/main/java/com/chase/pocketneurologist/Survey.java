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
import android.widget.Spinner;

/**
 * Created by Chase Dawson on 8/30/2017.
 */

public class Survey extends AppCompatActivity {

    private String[] ageRanges;
    private String[] sex;
    private ViewGroup rootScene;
    private Scene firstScene;
    private Scene secondScene;
    private Spinner[] spinners;
    private ArrayAdapter<String>[] adapters;
    private int currentQuestion = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

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
        adapters[1] = new ArrayAdapter<String>(this, R.layout.spinner_item, sex);

    }

    public void nextQuestion(View v) {
        currentQuestion++;
        Transition fade  = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        TransitionManager.go(secondScene, fade);
        spinners[currentQuestion] = (Spinner) findViewById(R.id.spinner);
        spinners[currentQuestion].setAdapter(adapters[currentQuestion]);
    }
}
