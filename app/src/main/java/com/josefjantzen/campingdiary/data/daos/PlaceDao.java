package com.josefjantzen.campingdiary.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.josefjantzen.campingdiary.data.entities.Place;
import com.josefjantzen.campingdiary.data.relations.FullPlace;

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

    @Delete
    void deletePlace(Place place);

    @Query("DELETE FROM events_table WHERE id = :placeId")
    void deletePlace(int placeId);
}
