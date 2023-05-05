package com.josefjantzen.campingdiary.ui.events.allEvents;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllEventsViewModelFactory implements ViewModelProvider.Factory{

    private final Application mApplication;
    private final int mReiseId;

    public AllEventsViewModelFactory(Application application, int reiseId) {
        mApplication = application;
        mReiseId = reiseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllEventsViewModel(mApplication, mReiseId);
    }
}
