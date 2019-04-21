package net.astatis.reddot.lib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asia on 2017/7/29.
 */

public class SharedPreferenceDataStore implements DataStore {

    private SharedPreferences mSharedPreferences;

    public SharedPreferenceDataStore(Context context) {
        mSharedPreferences = context.getSharedPreferences("pref_red_dots", Context.MODE_PRIVATE);
    }


    @Override
    public boolean restore(RedDot redDot) {
        String key = redDot.getPathString();
        String visibleKey = key + ":visible";
        String numberKey = key + ":number";
        boolean contains = mSharedPreferences.contains(visibleKey);
        if (contains) {
            redDot.setVisible(mSharedPreferences.getBoolean(visibleKey, false));
        }
        contains = mSharedPreferences.contains(numberKey);
        if (contains) {
            redDot.setNumber(mSharedPreferences.getInt(numberKey, 0));
        }
        return false;
    }

    @Override
    public void save(RedDot redDot) {
        String key = redDot.getPathString();
        String visibleKey = key + ":visible";
        String numberKey = key + ":number";
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(numberKey, redDot.getNumber());
        edit.putBoolean(visibleKey, redDot.isVisible());
        edit.apply();
    }
}
