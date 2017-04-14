package android.ctm.com.recyclertest.persistence;

import android.app.Activity;
import android.content.SharedPreferences;
import android.ctm.com.recyclertest.App;

/**
 * Created by Paul on 4/13/17.
 */

public class Prefs {

    private static final String LOG_TAG = Prefs.class.getName();

    private static final String PREF_FILE = "prefs";

    private static final String KEY_URL_ID = "url_id";

    public static String getStringPref(String key, String defaultVal){
        return getStringPref(key, defaultVal);
    }

    public static String getStringPref(String key){
        SharedPreferences sp = App.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
        String strVal = sp.getString(key, null);
        return strVal;
    }

    public static void saveStringPref(String key, String val){
        SharedPreferences sp = App.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void saveIntPref(String key, int val){
        SharedPreferences sp = App.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static int getIntPref(String key){
        SharedPreferences sp = App.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
        int intVal = sp.getInt(key , -1);
        return intVal;
    }

    public static String getKeyUrlPref(){
        return getStringPref(KEY_URL_ID);
    }

    public static void saveKeyUrlPref(String val){
        saveStringPref(KEY_URL_ID, val);
    }
}
