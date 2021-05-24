package com.ionshield.planner.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

@Database(entities = {
        Plan.class,
        Note.class,
        Constraint.class,
        Event.class,
        Item.class,
        Type.class,
        Location.class,
        Node.class,
        TypeItemRef.class,
        LocationTypeRef.class
},
        version = 2
)
@TypeConverters({com.ionshield.planner.database.TypeConverters.class})
public abstract class PlannerDatabase extends RoomDatabase {
    public abstract PlanDao planDao();
    public abstract NoteDao noteDao();
    public abstract ConstraintDao constraintDao();
    public abstract EventDao eventDao();
    public abstract ItemDao itemDao();
    public abstract TypeDao typeDao();
    public abstract LocationDao locationDao();
    public abstract NodeDao nodeDao();
    public abstract TypeItemRefDao typeItemRefDao();
    public abstract LocationTypeRefDao locationTypeRefDao();

    private static volatile PlannerDatabase INSTANCE;

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE 'event';");
        }
    };


    public static PlannerDatabase getDatabase() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Database is not initialized");
        }
        return INSTANCE;
    }

    public static boolean isReady() {
        return INSTANCE != null;
    }

    public static boolean init(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PlannerDatabase.class, "planner_database")
                            .fallbackToDestructiveMigration()
                            .build();
                    return true;
                }
            }
        }
        return false;
    }

}


