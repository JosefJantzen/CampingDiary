package com.josefjantzen.campingdiary.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.josefjantzen.campingdiary.data.RoomDatabase;
import com.josefjantzen.campingdiary.data.daos.PlaceDao;
import com.josefjantzen.campingdiary.data.entities.Place;
import com.josefjantzen.campingdiary.data.relations.FullPlace;

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

    public void updatePlace(List<Place> places) {
        for (Place place : places) {
            RoomDatabase.databaseWriteExecutor.execute(() -> mPlaceDao.updatePlace(place));
        }
    }

    public void deletePlace(Place place) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mPlaceDao.deletePlace(place));
    }
    public void deletePlace(int placeId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mPlaceDao.deletePlace(placeId));
    }
}
