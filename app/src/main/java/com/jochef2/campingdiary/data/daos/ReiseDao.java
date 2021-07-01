package com.jochef2.campingdiary.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;
import com.jochef2.campingdiary.data.relations.FullReise;

import java.util.List;

@Dao
public interface ReiseDao {

    @Transaction
    @Query("SELECT * FROM reisen_table ORDER BY id")
    LiveData<List<FullReise>> getAllReisen();

    @Transaction
    @Query("SELECT * FROM reisen_table WHERE  id = :reiseId")
    LiveData<FullReise> getReise(int reiseId);

    @Transaction
    @Query("SELECT * FROM reisen_table ORDER BY id DESC LIMIT 1")
    LiveData<FullReise> getCurrentReise();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReise(Reise reise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNight(Night night);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFuel(Fuel fuel);

    @Update
    void updateReise(Reise... reise);

    @Update
    void updateNight(Night... night);

    @Update
    void updateEvent(Event... event);

    @Update
    void updateSupplyAndDisposal(SupplyAndDisposal... supplyAndDisposal);

    @Update
    void updateFuel(Fuel... fuel);

    @Query("DELETE FROM reisen_table")
    void deleteAll();
}
