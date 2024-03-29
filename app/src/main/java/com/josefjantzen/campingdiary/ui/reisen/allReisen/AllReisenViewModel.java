package com.josefjantzen.campingdiary.ui.reisen.allReisen;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.josefjantzen.campingdiary.data.entities.Event;
import com.josefjantzen.campingdiary.data.entities.Fuel;
import com.josefjantzen.campingdiary.data.entities.Night;
import com.josefjantzen.campingdiary.data.entities.Reise;
import com.josefjantzen.campingdiary.data.entities.SupplyAndDisposal;
import com.josefjantzen.campingdiary.data.relations.FullReise;
import com.josefjantzen.campingdiary.data.repositories.ReisenRepository;
import com.josefjantzen.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

import java.util.Calendar;
import java.util.List;

public class AllReisenViewModel extends AndroidViewModel {

    public static ReisenRepository mReisenRepository;
    private final LiveData<List<FullReise>> mAllReisen;

    /**
     * initials mAllReisen from db
     * initials mReisenRepository and mPrefrences
     *
     * @param application current application used for Context
     */
    public AllReisenViewModel(Application application) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mAllReisen = mReisenRepository.getAllReisen();
    }

    public boolean hasCurrentReise() {
        final boolean[] state = {false};
        mReisenRepository.getCurrentReise().observeForever(fullReise -> {
            state[0] = fullReise != null && fullReise.mReise.getBegin().before(Calendar.getInstance()) && fullReise.mReise.getEnd().after(Calendar.getInstance());
        });
        return state[0];

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