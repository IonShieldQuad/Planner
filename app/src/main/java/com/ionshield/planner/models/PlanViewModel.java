package com.ionshield.planner.models;


import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlanViewModel extends ViewModel {
    private SQLiteDatabase mDatabase;

    private final MutableLiveData<Integer> planId = new MutableLiveData<>();
    private final MutableLiveData<String> planName = new MutableLiveData<>();

    public PlanViewModel() {
        super();
        //SQLiteOpenHelper helper = new DatabaseHelper();
        //mDatabase = helper.getReadableDatabase();
    }

    public LiveData<Integer> getPlanId() {
        return planId;
    }

    public void setPlanId(int id) {
        planId.setValue(id);
        updatePlanName();
    }

    public LiveData<String> getPlanName() {
        return planName;
    }

    private void updatePlanName() {
        //TODO
    }
}
