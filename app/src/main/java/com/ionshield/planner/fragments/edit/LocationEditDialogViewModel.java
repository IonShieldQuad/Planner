package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.LocationDao;
import com.ionshield.planner.database.dao.LocationTypeRefDao;
import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.database.entities.compound.LocationWithTypes;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocationEditDialogViewModel extends ViewModel {
    private final MutableLiveData<LocationWithTypes> location = new MutableLiveData<>();
    private final MutableLiveData<Type> type = new MutableLiveData<>();
    private final MutableLiveData<LocationTypeRef> ref = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public LocationEditDialogViewModel() {
        LocationWithTypes locationWithTypes = new LocationWithTypes();
        locationWithTypes.location = new Location();
        location.setValue(locationWithTypes);
        type.setValue(new Type());
        ref.setValue(new LocationTypeRef());
    }

    public void setLocationById(long id, long nodeId) {
        ListenableFuture<List<LocationWithTypes>> res = PlannerRepository.getInstance().getLocationDao().queryLocationsWithTypes(id);
        res.addListener(() -> {
            try {
                List<LocationWithTypes> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (location) {
                        LocationWithTypes locationWithTypes = list.get(0);
                        locationWithTypes.location.nodeId = nodeId;
                        location.postValue(locationWithTypes);
                        if (locationWithTypes.types != null && !locationWithTypes.types.isEmpty()) {
                            type.postValue(locationWithTypes.types.get(0));
                            ListenableFuture<List<LocationTypeRef>> future = PlannerRepository.getInstance().getLocationTypeRefDao().queryLocationTypeRef(locationWithTypes.location.locationId, locationWithTypes.types.get(0).typeId);
                            List<LocationTypeRef> refs = future.get();
                            if (refs != null && !refs.isEmpty()) {
                                ref.postValue(refs.get(0));
                            }
                        }
                    }
                }
                else {
                    LocationWithTypes locationWithTypes = new LocationWithTypes();
                    locationWithTypes.location = new Location();
                    locationWithTypes.location.nodeId = nodeId;
                    location.postValue(locationWithTypes);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeLocation() {
        LocationDao dao = PlannerRepository.getInstance().getLocationDao();
        LocationTypeRefDao locationTypeRefDao = PlannerRepository.getInstance().getLocationTypeRefDao();
        LocationWithTypes value = location.getValue();
        LocationTypeRef ltr = ref.getValue();
        Type typeValue = type.getValue();
        if (value != null && ltr != null && type.getValue() != null) {
            if (value.location.locationId == 0) {
                ListenableFuture<List<Long>> future = dao.insertAll(value.location);
                future.addListener(() -> {
                    try {
                        List<Long> rowIds = future.get();
                        if (rowIds != null && !rowIds.isEmpty()) {
                            ltr.locationId = rowIds.get(0);
                            ltr.typeId = type.getValue().typeId;
                            if (ltr.LocationTypeRefId == 0) {
                                locationTypeRefDao.insertAll(ltr);
                            } else {
                                locationTypeRefDao.updateAll(ltr);
                            }
                        }
                    }
                    catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }, executor);
            }
            else {
                dao.updateAll(value.location);
                ltr.locationId = value.location.locationId;
                ltr.typeId = typeValue.typeId;
                if (ltr.LocationTypeRefId == 0) {
                    locationTypeRefDao.insertAll(ltr);
                }
                else {
                    locationTypeRefDao.updateAll(ltr);
                }
            }

        }


    }

    public LiveData<LocationWithTypes> getLocation() {
        return location;
    }

    public void setName(String name) {
        if (location.getValue() != null) {
            synchronized (location) {
                location.getValue().location.name = name;
                location.setValue(location.getValue());
            }
        }
    }

    public void setDescription(String description) {
        if (location.getValue() != null) {
            synchronized (location) {
                location.getValue().location.description = description;
                location.setValue(location.getValue());
            }
        }
    }

    public void setType(Type type) {
        this.type.setValue(type);
    }

    public LiveData<Type> getType() {
        return type;
    }
}