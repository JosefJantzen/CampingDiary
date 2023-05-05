package com.jochef2.campingdiary.ui.prices.allPrices;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jochef2.campingdiary.data.relations.FullReise;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

public class AllPricesViewModel extends AndroidViewModel {

    private static ReisenRepository mReisenRepository;
    public LiveData<FullReise> mReise;

    public AllPricesViewModel(@NonNull Application application, int reiseId) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mReise = mReisenRepository.getReise(reiseId);
    }
}