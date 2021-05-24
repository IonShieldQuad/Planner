package com.ionshield.planner.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataTabViewModel extends ViewModel {
    private final MutableLiveData<Integer> page = new MutableLiveData<>();

    public DataTabViewModel() {
        page.setValue(0);
    }

    public LiveData<Integer> getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page.setValue(page);
    }
}
