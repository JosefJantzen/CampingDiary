package com.jochef2.campingdiary.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jochef2.campingdiary.data.RoomDatabase;
import com.jochef2.campingdiary.data.daos.PlaceDao;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.data.relations.FullPlace;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlaceRepository {

    private final PlaceDao mPlaceDao;
    private final LiveData<List<FullPlace>> mAllPlaces;
    private final LiveData<List<Place>> mAllShortPlaces;

    public PlaceRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mPlaceDao = db.placeDao();
        mAllPlaces = mPlaceDao.getAllPlaces();
        mAllShortPlaces = mPlaceDao.getAllShortPlaces();
    }

    public LiveData<List<FullPlace>> getAllPlaces() {
        return mAllPlaces;
    }

    public LiveData<List<Place>> getAllShortPlaces() {
        return mAllShortPlaces;
    }

    public LiveData<FullPlace> getPlace(int id) {
        return mPlaceDao.getPlace(id);
    }

    /**
     * inserts a new Place
     *
     * @param place new Place
     * @return id of new Place
     */
    public long insertPlace(Place place) {
        Callable<Long> insertCallable = () -> mPlaceDao.insertPlace(place);
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

    public void updatePlace(Place place) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mPlaceDao.insertPlace(place));
    }
}
