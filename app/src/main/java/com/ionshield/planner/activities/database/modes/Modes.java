package com.ionshield.planner.activities.database.modes;

public enum Modes {
    ITEMS(new ItemsMode()), TYPES(new TypesMode()), ITEM_ASSOC(new ItemAssocMode()), NODES(new NodesMode()), LOCATIONS(new LocationsMode()), EVENTS(new EventsMode()), PLANS(new PlansMode()), EVENT_ASSOC(new EventAssocMode()), LINK_TYPES(new LinkTypesMode()), LINKS(new LinksMode());
    public Mode mode;
    Modes(Mode mode) {
        this.mode = mode;
    }
}
