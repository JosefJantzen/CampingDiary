package com.josefjantzen.campingdiary.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.josefjantzen.campingdiary.data.RoomDatabase;
import com.josefjantzen.campingdiary.data.daos.ReiseDao;
import com.josefjantzen.campingdiary.data.entities.Event;
import com.josefjantzen.campingdiary.data.entities.Fuel;
import com.josefjantzen.campingdiary.data.entities.Night;
import com.josefjantzen.campingdiary.data.entities.Reise;
import com.josefjantzen.campingdiary.data.entities.Route;
import com.josefjantzen.campingdiary.data.entities.SupplyAndDisposal;
import com.josefjantzen.campingdiary.data.relations.FullReise;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReisenRepository {

    private final ReiseDao mReiseDao;
    private final LiveData<List<FullReise>> mAllReisen;
    public static RoomDatabase db;

    public ReisenRepository(Application application) {
        db = RoomDatabase.getDatabase(application);
        mReiseDao = db.reiseDao();
        mAllReisen = mReiseDao.getAllReisen();
    }

    public LiveData<List<FullReise>> getAllReisen() {
        return mAllReisen;
    }

    public LiveData<FullReise> getReise(int reiseId) {
        return mReiseDao.getReise(reiseId);
    }

    public LiveData<FullReise> getCurrentReise() {
        return mReiseDao.getCurrentReise();
    }

    /**
     * inserts new reise
     *
     * @param reise new Reise
     * @return id of new Reise
     */
    public long insertReise(Reise reise) {
        Callable<Long> insertCallable = () -> mReiseDao.insertReise(reise);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        long id = -1;
        Future<Long> future = executorService.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertNight(Night night) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.insertNight(night));
    }

    public void insertEvent(Event event) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.insertEvent(event));
    }

    public void insertSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.insertSupplyAndDisposal(supplyAndDisposal));
    }

    public void insertFuel(Fuel fuel) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.insertFuel(fuel));
    }

    public void insertRoute(Route route) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.insertRoute(route));
    }

    public void updateReise(Reise reise) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.updateReise(reise));
    }

    public void updateNight(Night night) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.updateNight(night));
    }

    public void updateEvent(Event event) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.updateEvent(event));
    }

    public void updateSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.updateSupplyAndDisposal(supplyAndDisposal));
    }

    public void updateFuel(Fuel fuel) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.updateFuel(fuel));
    }

    public void updateRoute(Route route) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.updateRoute(route));
    }

    public void deleteReise(Reise reise) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteReise(reise));
    }
    public void deleteReise(int reiseId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteReise(reiseId));
    }
    public void deleteNight(Night night) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteNight(night));
    }
    public void deleteNight(int nightId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteNight(nightId));
    }
    public void deleteEvent(Event event) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteEvent(event));
    }
    public void deleteEvent(int eventId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteEvent(eventId));
    }
    public void deleteSAD(SupplyAndDisposal sad) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteSAD(sad));
    }
    public void deleteSAD(int sadId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteSAD(sadId));
    }
    public void deleteFuel(Fuel fuel) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteFuel(fuel));
    }
    public void deleteFuel(int fuelId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteFuel(fuelId));
    }

    public void deleteRoute(Route route) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteRoute(route));
    }

    public void deleteRoute(int routeId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mReiseDao.deleteRoute(routeId));
    }
    
    /**
     * deletes all reisen in db
     */
    public void deleteAllReisen() {
        RoomDatabase.databaseWriteExecutor.execute(mReiseDao::deleteAll);
    }
}
