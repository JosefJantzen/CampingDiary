package com.jochef2.campingdiary.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.data.relations.FullPlace;

import java.util.List;

@Dao
public interface PlaceDao {

    @Transaction
    @Query("SELECT * FROM places_table ORDER BY id")
    LiveData<List<FullPlace>> getAllPlaces();

    @Query("SELECT * FROM places_table ORDER BY id")
    LiveData<List<Place>> getAllShortPlaces();

    @Transaction
    @Query("SELECT * FROM places_table WHERE id = :placeId")
    LiveData<FullPlace> getPlace(int placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlace(Place place);

    @Update
    void updatePlace(Place... place);

}
