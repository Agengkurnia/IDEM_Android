package app.mi2014.idem.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import app.mi2014.idem.app.AppController;

public class PrefUtils {

    public static final String KEY_USER_NIM = "user.nim";
    public static final String KEY_USER_NAME = "user.name";
    public static final String KEY_USER_PICTURE = "user.picture";
    public static final String KEY_APP_SESSION = "app.session";
    public static final String KEY_APP_TOKEN = "app.token";
    public static final String KEY_ACTIVE_COURSE = "user.courses";

    public static void save(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getBaseContext());
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void save(String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getBaseContext());
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void save(String key, Boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getBaseContext());
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String get(String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getBaseContext());
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static int get(String key, int defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getBaseContext());
        try {
            return sharedPrefs.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static boolean get(String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getBaseContext());
        try {
            return sharedPrefs.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

}