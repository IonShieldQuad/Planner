package com.ionshield.planner.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.entities.Node;
import com.ionshield.planner.database.entities.compound.NodeWithLocations;

import java.util.List;

public class MapFragmentViewModel extends ViewModel {
    private final LiveData<List<NodeWithLocations>> data;
    private final MutableLiveData<Boolean> mode = new MutableLiveData<>();
    private final MutableLiveData<CameraPosition> cameraPosition = new MutableLiveData<>();

    public MapFragmentViewModel() {
        data = Transformations.distinctUntilChanged(PlannerRepository.getInstance().getNodeDao().liveNodesWithLocations());
        mode.setValue(false);
    }

    @NonNull
    public LiveData<List<NodeWithLocations>> getData() {
        return data;
    }

    @NonNull
    public LiveData<Boolean> getMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode.setValue(mode);
    }

    @NonNull
    public LiveData<CameraPosition> getCameraPosition() {
        return Transformations.distinctUntilChanged(cameraPosition);
    }

    public void setCameraPosition(CameraPosition position) {
        cameraPosition.setValue(position);
    }

    public void updateNode(Node node) {
        PlannerRepository.getInstance().getNodeDao().updateAll(node);
    }

    public void addNode(LatLng location) {
        Node node = new Node();
        node.latitude = location.latitude;
        node.longitude = location.longitude;
        PlannerRepository.getInstance().getNodeDao().insertAll(node);
    }

    public void deleteNode(Node node) {
        PlannerRepository.getInstance().getNodeDao().deleteAll(node);
    }
}
