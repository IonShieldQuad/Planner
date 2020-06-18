package com.ionshield.planner.activities.database.modes;

public enum Modes {
    ITEMS(new ItemsMode()), TYPES(new TypesMode()), ITEM_ASSOC(new ItemAssocMode()), LOCATIONS(new LocationsMode()), EVENTS(new EventsMode()), PLANS(new PlansMode()), EVENT_ASSOC(new EventAssocMode());
    public Mode mode;
    Modes(Mode mode) {
        this.mode = mode;
    }
}
