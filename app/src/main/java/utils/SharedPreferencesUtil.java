package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesUtil {

    private static final String SP_NAME = "sp_name";
    /**
     * 夜间模式
     */
    public static final String SP_DARK_MODE = "sp_dark_mode";

    public static SharedPreferences getPreference() {
        return CowboyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        try {
            return getPreference().getBoolean(key, defValue);
        } catch (NullPointerException exception) {
            Log.d("zpf", "" + exception);
            return defValue;
        }
    }

    public static void putBoolean(String key, boolean value) {
        try {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putBoolean(key, value);
            editor.apply();
        } catch (NullPointerException exception) {
            Log.d("zpf", "" + exception);
        }
    }


    public long getLong(String key, long defValue) {
        try {
            return getPreference().getLong(key, defValue);
        } catch (NullPointerException exception) {
            Log.d("zpf", "" + exception);
            return defValue;
        }
    }

    public void putLong(String key, long value) {
        try {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putLong(key, value);
            editor.apply();
        } catch (NullPointerException exception) {
            Log.d("zpf", "" + exception);
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return getPreference().getInt(key, defaultValue);
        } catch (Exception e) {
            Log.d("zpf", "" + e);
            return defaultValue;

        }
    }

    public void putInt(String key, int value) {
        try {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putInt(key, value);
            editor.apply();
        } catch (Exception e) {
            Log.d("zpf", "" + e);
        }
    }

    public static String getStrings(String key, String defValue) {
        try {
            return getPreference().getString(key, defValue);
        } catch (NullPointerException e) {
            Log.d("zpf", "" + e);
            return defValue;
        }
    }

    public static boolean putString(String key, String value) {
        try {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putString(key, value);
            editor.apply();
            return true;
        } catch (NullPointerException e) {
            Log.d("zpf", "" + e);
            return false;
        }
    }

    public void clear() {
        try {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.clear();
            editor.apply();
        } catch (NullPointerException e) {
            Log.d("zpf", "" + e);
        }
    }
}
