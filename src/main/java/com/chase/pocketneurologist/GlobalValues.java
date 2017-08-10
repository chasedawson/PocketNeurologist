package com.chase.pocketneurologist;

import android.util.Log;

/**
 * Created by Chase Dawson on 6/27/2017.
 */

public class GlobalValues {
    private static boolean testMode = false;
    private static String userID = null;
    private static String testID = null;

    public static boolean getTestMode() {
        Log.i("TEST MODE", testMode + "");
        return testMode;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserId(String str) {
        userID = str;
    }

    public static void setTestMode(boolean b) {
        testMode = b;
    }

    public static String getTestID() {
        return testID;
    }

    public static void setTestID(String str) {
        testID = str;
    }




}
