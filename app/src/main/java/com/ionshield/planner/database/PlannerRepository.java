package com.ionshield.planner.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ionshield.planner.database.dao.ConstraintDao;
import com.ionshield.planner.database.dao.EventDao;
import com.ionshield.planner.database.dao.ItemDao;
import com.ionshield.planner.database.dao.LocationDao;
import com.ionshield.planner.database.dao.LocationTypeRefDao;
import com.ionshield.planner.database.dao.NodeDao;
import com.ionshield.planner.database.dao.NoteDao;
import com.ionshield.planner.database.dao.PlanDao;
import com.ionshield.planner.database.dao.TypeDao;
import com.ionshield.planner.database.dao.TypeItemRefDao;
import com.ionshield.planner.database.entities.Constraint;
import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.Node;
import com.ionshield.planner.database.entities.Note;
import com.ionshield.planner.database.entities.Plan;
import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.database.entities.TypeItemRef;

import java.util.List;

public class PlannerRepository {
    private static PlannerRepository INSTANCE;

    private PlanDao planDao;
    private NoteDao noteDao;
    private ConstraintDao constraintDao;
    private EventDao eventDao;
    private ItemDao itemDao;
    private TypeDao typeDao;
    private LocationDao locationDao;
    private NodeDao nodeDao;
    private TypeItemRefDao typeItemRefDao;
    private LocationTypeRefDao locationTypeRefDao;

    private LiveData<List<Plan>> allPlans;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Constraint>> allConstraints;
    private LiveData<List<Event>> allEvents;
    private LiveData<List<Item>> allItems;
    private LiveData<List<Type>> allTypes;
    private LiveData<List<Location>> allLocations;
    private LiveData<List<Node>> allNodes;
    private LiveData<List<TypeItemRef>> allTypeItemRefs;
    private LiveData<List<LocationTypeRef>> allLocationTypeRefs;

    private PlannerRepository() {
        PlannerDatabase db = PlannerDatabase.getDatabase();

        planDao = db.planDao();
        noteDao = db.noteDao();
        constraintDao = db.constraintDao();
        eventDao = db.eventDao();
        itemDao = db.itemDao();
        typeDao = db.typeDao();
        locationDao = db.locationDao();
        nodeDao = db.nodeDao();
        typeItemRefDao = db.typeItemRefDao();
        locationTypeRefDao = db.locationTypeRefDao();

        allPlans = Transformations.distinctUntilChanged(planDao.livePlans());
        allNotes = Transformations.distinctUntilChanged(noteDao.liveNotes());
        allConstraints = Transformations.distinctUntilChanged(constraintDao.liveConstraints());
        allEvents = Transformations.distinctUntilChanged(eventDao.liveEvents());
        allItems = Transformations.distinctUntilChanged(itemDao.liveItems());
        allTypes = Transformations.distinctUntilChanged(typeDao.liveTypes());
        allLocations = Transformations.distinctUntilChanged(locationDao.liveLocations());
        allNodes = Transformations.distinctUntilChanged(nodeDao.liveNodes());
        allTypeItemRefs = Transformations.distinctUntilChanged(typeItemRefDao.liveTypeItemRefs());
        allLocationTypeRefs = Transformations.distinctUntilChanged(locationTypeRefDao.liveLocationTypeRefs());
    }

    public static PlannerRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlannerRepository();
        }
        return INSTANCE;
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public ConstraintDao getConstraintDao() {
        return constraintDao;
    }

    public EventDao getEventDao() {
        return eventDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public TypeDao getTypeDao() {
        return typeDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public NodeDao getNodeDao() {
        return nodeDao;
    }

    public TypeItemRefDao getTypeItemRefDao() {
        return typeItemRefDao;
    }

    public LocationTypeRefDao getLocationTypeRefDao() {
        return locationTypeRefDao;
    }

    public LiveData<List<Plan>> getAllPlans() {
        return allPlans;
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Constraint>> getAllConstraints() {
        return allConstraints;
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Type>> getAllTypes() {
        return allTypes;
    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }

    public LiveData<List<Node>> getAllNodes() {
        return allNodes;
    }

    public LiveData<List<TypeItemRef>> getAllTypeItemRefs() {
        return allTypeItemRefs;
    }

    public LiveData<List<LocationTypeRef>> getAllLocationTypeRefs() {
        return allLocationTypeRefs;
    }
}
