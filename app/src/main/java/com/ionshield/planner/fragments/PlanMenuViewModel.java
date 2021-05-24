package com.ionshield.planner.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ionshield.planner.database.entities.Plan;

public class PlanMenuViewModel extends ViewModel {
    private final MutableLiveData<Plan> plan = new MutableLiveData<>();

    public LiveData<Plan> getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        synchronized (this.plan) {
            this.plan.setValue(plan);
        }
    }
}