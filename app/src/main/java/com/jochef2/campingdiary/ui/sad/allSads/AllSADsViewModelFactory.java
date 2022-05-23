package com.jochef2.campingdiary.ui.sad.allSads;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllSADsViewModelFactory implements ViewModelProvider.Factory{

    private final Application mApplication;
    private final int mReiseId;

    public AllSADsViewModelFactory(Application application, int reiseId) {
        mApplication = application;
        mReiseId = reiseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllSADsViewModel(mApplication, mReiseId);
    }
}
