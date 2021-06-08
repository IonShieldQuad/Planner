package com.ionshield.planner.math;

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.compound.LocationAndNode;

import java.util.Objects;

public class EventWithData {
    public Event event;
    public LocationAndNode location;

    public EventWithData() {
    }

    public EventWithData(Event event, LocationAndNode location) {
        this.event = event;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventWithData that = (EventWithData) o;
        return Objects.equals(event, that.event) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, location);
    }
}
