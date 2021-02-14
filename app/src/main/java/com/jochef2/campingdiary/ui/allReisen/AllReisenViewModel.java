package com.jochef2.campingdiary.ui.allReisen;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jochef2.campingdiary.data.ReisenRepository;
import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.FullReise;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

import java.util.List;

public class AllReisenViewModel extends AndroidViewModel {

    public static ReisenRepository mReisenRepository;
    private final LiveData<List<FullReise>> mAllReisen;
    private SharedPreferences mPreferences;

    /**
     * initials mAllReisen from db
     * initials mReisenRepository and mPrefrences
     *
     * @param application current application used for Context
     */
    public AllReisenViewModel(Application application) {
        super(application);
        mReisenRepository = new ReisenRepository(application);
        mAllReisen = mReisenRepository.getAllReisen();
        mPreferences = application.getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
    }

    /**
     * @return id of current reise
     */
    public String getCurrentId() {
        return mPreferences.getString("CURRENT_REISE", "-1");
    }

    LiveData<List<FullReise>> getAllReisen() {
        return mAllReisen;
    }

    LiveData<FullReise> getReise(int reiseId) {
        return mReisenRepository.getReise(reiseId);
    }

    public void insertReise(Reise reise) {
        mReisenRepository.insertReise(reise);
    }

    public void insertNight(Night night) {
        mReisenRepository.insertNight(night);
    }

    public void insertEvent(Event event) {
        mReisenRepository.insertEvent(event);
    }

    public void insertSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal) {
        mReisenRepository.insertSupplyAndDisposal(supplyAndDisposal);
    }

    public void insertFuel(Fuel fuel) {
        mReisenRepository.insertFuel(fuel);
    }

    public void updateReise(Reise reise) {
        mReisenRepository.updateReise(reise);
    }

    public void updateNight(Night night) {
        mReisenRepository.updateNight(night);
    }

    public void updateEvent(Event event) {
        mReisenRepository.updateEvent(event);
    }

    public void updateSupplyAndDisposal(SupplyAndDisposal supplyAndDisposal) {
        mReisenRepository.updateSupplyAndDisposal(supplyAndDisposal);
    }

    public void updateFuel(Fuel fuel) {
        mReisenRepository.updateFuel(fuel);
    }

    public void deleteAllReisen() {
        mReisenRepository.deleteAllReisen();
    }
}