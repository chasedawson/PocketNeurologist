package com.chase.pocketneurologist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: Displays the possible tests and a short description of each to the user. When an item is selected the respective test start screen appears.
 */

public class TestList extends AppCompatActivity {

    ListView listView;
    ArrayList<Map<String, String>> data = new ArrayList<>();

    //list of available tests
    String[] tests = {"Gait Length", "Gait Acceleration", "Tremor", "Rigidity", ""};

    //descriptions of each test
    String[] description = {"Uses pedometer and GPS to estimate gait length.", "Measures acceleration while walking.", "Measures tremor intensity in the hand.", "Measures rigidity intensity in arms while walking.", ""};

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for(int x = 0; x < tests.length; x++) {
            Map<String, String> map = new HashMap<String, String>(2);
            map.put("title", tests[x]);
            map.put("description", description[x]);
            data.add(map);
        }


        setContentView(R.layout.activity_test_list);

        listView = (ListView) findViewById(android.R.id.list);

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"title", "description"}, new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if(tests[position].equals("Gait (Gait Length)"))
                    intent = new Intent(TestList.this, GaitTest_GaitLength.class); */
                if(tests[position].equals("Gait Length"))
                    intent = new Intent(TestList.this, GaitTest_GaitLength.class);
                else if (tests[position].equals("Gait Acceleration"))
                    intent = new Intent(TestList.this, GaitTest_Acceleration.class);
                else if (tests[position].equals("Tremor"))
                    intent = new Intent(TestList.this, TremorTest.class);
                else if (tests[position].equals("Rigidity"))
                    intent = new Intent(TestList.this, RigidityTest.class);
                else if(tests[position].equals("")) {
                    intent = new Intent(TestList.this, TestList.class);
                }
                startActivity(intent);
            }
        });


    }
}