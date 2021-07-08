package com.jochef2.campingdiary.ui.fuel.allFuels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllFuelsViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplication;
    private int mReiseId;

    public AllFuelsViewModelFactory(Application application, int reiseId) {
        mApplication = application;
        mReiseId = reiseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllFuelsViewModel(mApplication, mReiseId);
    }
}
