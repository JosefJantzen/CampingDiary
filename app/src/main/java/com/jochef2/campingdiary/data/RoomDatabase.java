package com.jochef2.campingdiary.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jochef2.campingdiary.data.daos.PlaceDao;
import com.jochef2.campingdiary.data.daos.ReiseDao;
import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.Route;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Reise.class, Night.class, Event.class, SupplyAndDisposal.class, Fuel.class, Place.class, Route.class}, version = 40, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile RoomDatabase INSTANCE;
    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // write default data to db
            databaseWriteExecutor.execute(() -> {

                /*ReiseDao dao = INSTANCE.reiseDao();
                dao.deleteAll();
                Log.d("ASD", "ASDasfdafgag");
                /*Reise reise = new Reise(0, "Schweiz", "DE, CH", "Das ist eine Beschreibung");
                dao.insert(reise);*/
            });
        }
    };

    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "reisen_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ReiseDao reiseDao();

    public abstract PlaceDao placeDao();
}
