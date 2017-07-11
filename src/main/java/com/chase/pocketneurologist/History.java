package com.chase.pocketneurologist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Purpose: Displays a list of activities that display the test results. When an item is selected the respective test results screen appears.
 */
public class History extends AppCompatActivity {

    ListView listView;
    String[] tests = {"Gait (Gait Length)", "Gait (Acceleration)", "Tremor", "Rigidity", ""};

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = (ListView) findViewById(android.R.id.list);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1, tests);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tests[position].equals("Gait (Gait Length)"))
                    intent = new Intent(History.this, GaitTest_GaitLengthResults.class);
                else if(tests[position].equals("Gait (Acceleration)"))
                    intent = new Intent(History.this, GaitTest_AccelerationResults.class);
                else if (tests[position].equals("Tremor"))
                    intent = new Intent(History.this, TremorTestResults.class);
                else if (tests[position].equals("Rigidity"))
                    intent = new Intent(History.this, RigidityTestResults.class);
                else if(tests[position].equals("")) {
                    intent = new Intent(History.this, History.class);
                }
                startActivity(intent);
            }
        });


    }

    public void deleteAll(View view) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteAll();
    }
}
