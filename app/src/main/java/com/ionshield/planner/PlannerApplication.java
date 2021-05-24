package com.ionshield.planner;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.ionshield.planner.database.PlannerDatabase;

public class PlannerApplication extends Application {
    private static PlannerApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        PlannerDatabase.init(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        initDarkMode();
    }

    public static PlannerApplication getApplication() {
        return INSTANCE;
    }

    private void initDarkMode() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean mode = pref.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(mode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        pref.edit().putBoolean("dark_mode", mode).apply();
    }

    public boolean getDarkMode() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getBoolean("dark_mode", false);
        //int nightTheme = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);
        //return nightTheme == Configuration.UI_MODE_NIGHT_YES;

    }

    public void setDarkMode(boolean mode) {
        if (mode != getDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(mode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            pref.edit().putBoolean("dark_mode", mode).apply();
        }
    }

    public void toggleDarkMode() {
        setDarkMode(!getDarkMode());
    }
}
