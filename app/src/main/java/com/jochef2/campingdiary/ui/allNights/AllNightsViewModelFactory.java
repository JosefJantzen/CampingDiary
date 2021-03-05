package com.jochef2.campingdiary.ui.allNights;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllNightsViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private int mReiseId;

    public AllNightsViewModelFactory(Application application, int reiseId) {
        mApplication = application;
        mReiseId = reiseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllNightsViewModel(mApplication, mReiseId);
    }
}