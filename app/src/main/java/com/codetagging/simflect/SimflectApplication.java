package com.codetagging.simflect;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by govind on 26/05/16.
 */
public class SimflectApplication extends Application {

    private static SimflectApplication simflectApplication;
    private SharedPreferences sharedPreferences = null;

    public static SimflectApplication getInstance() {
        return simflectApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        simflectApplication = this;
        initializeSharedPreference();
    }

    private void initializeSharedPreference() {
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreference() {
        return sharedPreferences;
    }
}
