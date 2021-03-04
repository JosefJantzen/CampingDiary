package com.jochef2.campingdiary.ui.allNights;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jochef2.campingdiary.data.ReisenRepository;
import com.jochef2.campingdiary.data.entities.FullReise;
import com.jochef2.campingdiary.ui.main.CurrentReiseViewModel;

public class AllNightsViewModel extends AndroidViewModel {

    private static ReisenRepository mReisenRepository;
    public LiveData<FullReise> mReise;

    public AllNightsViewModel(@NonNull Application application, int reiseId) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mReise = mReisenRepository.getReise(reiseId);
    }

}