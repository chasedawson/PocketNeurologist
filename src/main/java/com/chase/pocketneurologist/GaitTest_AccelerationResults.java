package com.chase.pocketneurologist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * Created by chase on 3/15/17.
 *
 * Purpose: Subclass of TestResultsActivity that displays the results from the Gait (Acceleration) Test in a graphical format.
 *
 */

public class GaitTest_AccelerationResults extends TestResultsActivity {
    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTestResultType("Gait (Acceleration)");
        setGraphTitle("Acceleration (m/s/s) vs. Test Number");
        setUnit("m/s/s");
        setArrowColorGreenParameters(0, ">");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadGraph(ArrayList<TestResult> arrayList) {
        if(arrayList.size() == 0) {
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.create();
            dialogBuilder.setTitle("There currently are no results for this test.");
            dialogBuilder.setNegativeButton("close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            startActivity(new Intent(GaitTest_AccelerationResults.this, History.class));
                        }
                    });
            dialogBuilder.show();
        }
        else
            super.loadGraph(arrayList);
    }
}
