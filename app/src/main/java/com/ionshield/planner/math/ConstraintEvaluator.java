package com.ionshield.planner.math;

import com.ionshield.planner.database.entities.Constraint;

import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

public class ConstraintEvaluator {
    public static boolean testConstraints(List<EventWithDateTimeData> data, List<Constraint> constraints) {
        for (EventWithDateTimeData eventData : data) {
            if (eventData.event.isDateUsed) {
                if (eventData.event.datetimeMin != null && eventData.start.isBefore(ChronoLocalDateTime.from(eventData.event.datetimeMin))) {
                    return false;
                }
                if (eventData.event.datetimeMax != null && eventData.start.isAfter(ChronoLocalDateTime.from(eventData.event.datetimeMax))) {
                    return false;
                }
            }
            else {
                if (eventData.event.datetimeMin != null && eventData.start.toLocalTime().isBefore(eventData.event.datetimeMin.toLocalTime())) {
                    return false;
                }
                if (eventData.event.datetimeMax != null && eventData.start.toLocalTime().isAfter(eventData.event.datetimeMax.toLocalTime())) {
                    return false;
                }
            }

        }

        //TODO: use constraints
        return true;
    }
}
