package com.josefjantzen.campingdiary.ui.fuel.allFuels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.josefjantzen.campingdiary.data.relations.FullReise;
import com.josefjantzen.campingdiary.data.repositories.ReisenRepository;
import com.josefjantzen.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

public class AllFuelsViewModel extends AndroidViewModel {

    public static ReisenRepository mReisenRepository;
    public LiveData<FullReise> mReise;

    public AllFuelsViewModel(Application application, int reiseId) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mReise = mReisenRepository.getReise(reiseId);
    }
}