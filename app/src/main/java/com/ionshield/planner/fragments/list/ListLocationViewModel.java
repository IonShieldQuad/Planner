package com.ionshield.planner.fragments.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.entities.compound.LocationWithTypes;

import java.util.List;

public class ListLocationViewModel extends ViewModel {
    private final MutableLiveData<Mode> mode = new MutableLiveData<>();
    private final MutableLiveData<String> search = new MutableLiveData<>();
    private final LiveData<List<LocationWithTypes>> data;


    public ListLocationViewModel() {
        data = Transformations.switchMap(search, s -> {
            if (s == null || s.isEmpty()) {
                return Transformations.distinctUntilChanged(PlannerRepository.getInstance().getLocationDao().liveLocationsWithTypes());
            }
            else {
                return Transformations.distinctUntilChanged(PlannerRepository.getInstance().getLocationDao().liveLocationsWithTypes("%" + s + "%"));
            }
        });
        search.setValue("");
    }

    public LiveData<List<LocationWithTypes>> getData() {
        return data;
    }

    public void setMode(Mode mode) {
        this.mode.setValue(mode);
    }

    public LiveData<Mode> getMode() {
        return mode;
    }

    public void setSearch(String search) {
        if (!search.equals(this.search.getValue())) {
            this.search.setValue(search);
        }
    }

    public LiveData<String> getSearch() {
        return search;
    }

}
