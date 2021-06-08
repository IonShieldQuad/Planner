package com.ionshield.planner.math;

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.compound.LocationAndNode;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventWithDateTimeData {
    public Event event;
    public LocationAndNode location;

    public LocalDateTime start;
    public LocalDateTime finish;

    public EventWithDateTimeData() {
    }

    public EventWithDateTimeData(Event event, LocationAndNode location, LocalDateTime start, LocalDateTime finish) {
        this.event = event;
        this.location = location;
        this.start = start;
        this.finish = finish;
    }

    public EventWithDateTimeData(EventWithData data, LocalDateTime start, LocalDateTime finish) {
        this.event = data.event;
        this.location = data.location;
        this.start = start;
        this.finish = finish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventWithDateTimeData that = (EventWithDateTimeData) o;
        return Objects.equals(event, that.event) &&
                Objects.equals(location, that.location) &&
                Objects.equals(start, that.start) &&
                Objects.equals(finish, that.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, location, start, finish);
    }
}
