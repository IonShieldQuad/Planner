package com.ionshield.planner.math;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.ionshield.planner.database.PathRepository;
import com.ionshield.planner.database.entities.Node;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeEvaluator {
    public static TimeEvaluator.Result evaluate(LatLng origin, LocalDateTime startDateTime, List<EventWithData> events) {
        List<EventWithDateTimeData> outData = new ArrayList<>();
        LatLng currentLocation = origin;
        LocalDateTime currentTime = startDateTime;

        for (int i = 0; i < events.size(); i++) {
            EventWithData eventData = events.get(i);
            if (eventData == null || eventData.event == null) continue;

            if (eventData.location != null && eventData.location.node != null) {
                Node node = eventData.location.node;
                DirectionsResult result = PathRepository.getInstance().getPath(currentLocation, new LatLng(node.latitude, node.longitude));
                if (result == null || result.routes.length == 0) {
                    throw new IllegalArgumentException("Route to " + eventData.location.location.name + " not found");
                }
                else {
                    currentTime = currentTime.plusSeconds(result.routes[0].legs[0].duration.inSeconds);
                }
                currentLocation = new LatLng(node.latitude, node.longitude);
            }

            //Wait if needed
            if (eventData.event.isDateUsed) {
                if (eventData.event.datetimeMin != null && currentTime.isBefore(ChronoLocalDateTime.from(eventData.event.datetimeMin))) {
                    currentTime = LocalDateTime.from(eventData.event.datetimeMin);
                }
            }
            else {
                if (eventData.event.datetimeMin != null && currentTime.toLocalTime().isBefore(LocalTime.from(eventData.event.datetimeMin))) {
                    currentTime = LocalDateTime.from(eventData.event.datetimeMin);
                }
                if (eventData.event.datetimeMax != null && currentTime.toLocalTime().isAfter(LocalTime.from(eventData.event.datetimeMax))) {
                    currentTime = LocalDateTime.of(currentTime.toLocalDate().plusDays(1), eventData.event.datetimeMin != null ? eventData.event.datetimeMin.toLocalTime() : LocalTime.of(0, 0));
                }
            }

            outData.add(new EventWithDateTimeData(eventData, currentTime, currentTime.plus(eventData.event.duration)));
            currentTime = currentTime.plus(eventData.event.duration);
        }

        return new Result(Duration.between(startDateTime, currentTime), outData);
    }

    public static class Result {
        public Duration timeTaken;
        public List<EventWithDateTimeData> data;

        public Result() {
        }

        public Result(Duration timeTaken, List<EventWithDateTimeData> data) {
            this.timeTaken = timeTaken;
            this.data = data;
        }
    }
}
