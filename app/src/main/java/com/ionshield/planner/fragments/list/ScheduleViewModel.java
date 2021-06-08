package com.ionshield.planner.fragments.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.model.LatLng;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.entities.Constraint;
import com.ionshield.planner.database.entities.compound.EventAndItemWithTypesWithLocationsAndNodes;
import com.ionshield.planner.math.PermutationPlanOptimizer;
import com.ionshield.planner.math.PlanOptimizer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScheduleViewModel extends ViewModel {
    private final MutableLiveData<Long> planId = new MutableLiveData<>();
    private final PlanOptimizer optimizer = new PermutationPlanOptimizer();
    private final LiveData<Double> progress = optimizer.getProgress();
    private final MutableLiveData<PlanOptimizer.Result> result = new MutableLiveData<>();

    private final Executor executor = Executors.newSingleThreadExecutor();

    public void optimizePlan(long id, LatLng origin, LocalDateTime startTime) {
        if (planId.getValue() == null || id != planId.getValue()) {
            planId.setValue(id);
            if (id == 0) {
                result.setValue(null);
                return;
            }
            executor.execute(() -> {
                try {
                    List<Constraint> constraints = PlannerRepository.getInstance().getConstraintDao().queryConstraintsByPlanId(id).get();
                    long[] ids = PlannerRepository.getInstance().getEventDao().queryEventsByPlanId(id).get().stream().filter(event -> !event.isDone).mapToLong(event -> event.eventId).toArray();
                    List<EventAndItemWithTypesWithLocationsAndNodes> data = PlannerRepository.getInstance().getEventDao().queryEventsAndItemsWithTypesWithLocationsAndNodes(ids).get();
                    result.postValue(optimizer.optimize(origin, startTime, data, constraints));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    public LiveData<Long> getPlanId() {
        return planId;
    }

    public LiveData<Double> getProgress() {
        return progress;
    }

    public LiveData<PlanOptimizer.Result> getResult() {
        return result;
    }
}
