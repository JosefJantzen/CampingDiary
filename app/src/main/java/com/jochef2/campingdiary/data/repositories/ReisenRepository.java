package com.jochef2.campingdiary.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jochef2.campingdiary.data.RoomDatabase;
import com.jochef2.campingdiary.data.daos.ReiseDao;
import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;
import com.jochef2.campingdiary.data.relations.FullReise;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReisenRepository {

    private final ReiseDao mReiseDao;
    private final LiveData<List<FullReise>> mAllReisen;

    public ReisenRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
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
     * @return id of new reise
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

    /**
     * deletes all reisen in db
     */
    public void deleteAllReisen() {
        RoomDatabase.databaseWriteExecutor.execute(mReiseDao::deleteAll);
    }
}
