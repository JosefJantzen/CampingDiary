package com.jochef2.campingdiary.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.FullReise;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReisenRepository {

    private ReiseDao mReiseDao;
    private LiveData<List<FullReise>> mAllReisen;

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

    public long insertReise(Reise reise) {
        Callable<Long> insertCallable = () -> mReiseDao.insertReise(reise);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        long id = -1;
        Future<Long> future = executorService.submit(insertCallable);

        try {
            id = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertNight(Night night) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.insertNight(night);
        });
    }

    public void insertEvent(Event event) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.insertEvent(event);
        });
    }

    public void insertSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.insertSupplyAndDisposal(supplyAndDisposal);
        });
    }

    public void insertFuel(Fuel fuel) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.insertFuel(fuel);
        });
    }

    public void updateReise(Reise reise) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.updateReise(reise);
        });
    }

    public void updateNight(Night night) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.updateNight();
        });
    }

    public void updateEvent(Event event) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.updateEvent(event);
        });
    }

    public void updateSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.updateSupplyAndDisposal(supplyAndDisposal);
        });
    }

    public void updateFuel(Fuel fuel) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.updateFuel(fuel);
        });
    }

    public void deleteAllReisen() {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mReiseDao.deleteAll();
        });
    }
}
