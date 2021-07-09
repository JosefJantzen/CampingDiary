package com.jochef2.campingdiary.ui.route.allRoutes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllRoutesViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplication;
    private int mReiseId;

    public AllRoutesViewModelFactory(Application application, int reiseId) {
        mApplication = application;
        mReiseId = reiseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllRoutesViewModel(mApplication, mReiseId);
    }
}
