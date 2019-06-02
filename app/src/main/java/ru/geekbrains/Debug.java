package ru.geekbrains;

import android.util.Log;

public class Debug {
    private static String TAG = Debug.class.getSimpleName();
    private static boolean showDebugInfo = true;

    public static void logE(String tag, String message){
        if (showDebugInfo) Log.e(tag, message);
    }
}
