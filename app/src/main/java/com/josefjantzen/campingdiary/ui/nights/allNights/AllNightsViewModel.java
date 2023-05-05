package com.josefjantzen.campingdiary.ui.nights.allNights;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.josefjantzen.campingdiary.data.relations.FullReise;
import com.josefjantzen.campingdiary.data.repositories.ReisenRepository;
import com.josefjantzen.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

public class AllNightsViewModel extends AndroidViewModel {

    private static ReisenRepository mReisenRepository;
    public LiveData<FullReise> mReise;

    public AllNightsViewModel(@NonNull Application application, int reiseId) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mReise = mReisenRepository.getReise(reiseId);
    }
}