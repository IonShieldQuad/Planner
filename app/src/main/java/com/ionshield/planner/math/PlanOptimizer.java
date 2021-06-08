package com.ionshield.planner.math;

import androidx.lifecycle.LiveData;

import com.google.maps.model.LatLng;
import com.ionshield.planner.database.entities.Constraint;
import com.ionshield.planner.database.entities.compound.EventAndItemWithTypesWithLocationsAndNodes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanOptimizer {
    Result optimize(LatLng origin, LocalDateTime startTime, List<EventAndItemWithTypesWithLocationsAndNodes> data, List<Constraint> constraints);
    LiveData<Double> getProgress();

    class Result {
        public LatLng origin;
        public LocalDateTime startTime;
        public Duration timeTaken;
        public List<EventWithDateTimeData> data;

        public Result() {
        }

        public Result(LatLng origin, LocalDateTime startTime, Duration timeTaken, List<EventWithDateTimeData> data) {
            this.origin = origin;
            this.startTime = startTime;
            this.timeTaken = timeTaken;
            this.data = data;
        }
    }
}
