package com.jochef2.campingdiary.ui.fuel.allFuels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jochef2.campingdiary.data.relations.FullReise;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

public class AllFuelsViewModel extends ViewModel {

    public static ReisenRepository mReisenRepository;
    public LiveData<FullReise> mReise;

    public AllFuelsViewModel(Application application, int reiseId) {
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mReise = mReisenRepository.getReise(reiseId);
    }
}