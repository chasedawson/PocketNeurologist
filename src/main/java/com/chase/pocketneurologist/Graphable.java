package com.chase.pocketneurologist;

import java.util.ArrayList;

/**
 * Created by chase on 2/14/17.
 *
 * Purpose: Interface that defines the methods that must be implemented if the class plans on graphing TestResults.
 *
 */

public interface Graphable {
    public DatabaseHandler getDatabase();
    public void loadGraph(ArrayList<TestResult> arrayList);
    public void displayMoreInfo(TestResult testResult);
}
