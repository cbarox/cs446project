package ca.uwaterloo.mapapp.logic;

import android.util.Log;

/**
 * Created by cjbarrac
 * 6/7/15
 */
public class Logger {

    public static String TAG = "Whats nUW";

    public static void debug(String message) {
        Log.d(TAG, message);
    }

    public static void debug(String format, Object... args) {
        debug(String.format(format, args));
    }

    public static void error(String message) {
        Log.e(TAG, message);
    }

    public static void error(String format, Object... args) {
        error(String.format(format, args));
    }

    public static void error(String message, Throwable exception) {
        Log.e(TAG, message, exception);
    }

    public static void error(String format, Throwable exception, Object... args) {
        Log.e(TAG, String.format(format, args), exception);
    }

    public static void info(String message) {
        Log.i(TAG, message);
    }

    public static void info(String format, Object... args) {
        info(String.format(format, args));
    }

    public static void warning(String message) {
        Log.w(TAG, message);
    }

    public static void warning(String format, Object... args) {
        warning(String.format(format, args));
    }

    public static void wtf(String message) {
        Log.wtf(TAG, message);
    }

    public static void wtf(String format, Object... args) {
        wtf(String.format(format, args));
    }

    public static void wtf(String message, Throwable exception) {
        Log.wtf(TAG, message, exception);
    }

}