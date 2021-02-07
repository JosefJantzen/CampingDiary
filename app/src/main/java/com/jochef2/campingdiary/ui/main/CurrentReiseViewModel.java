package com.jochef2.campingdiary.ui.main;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.data.ReisenRepository;
import com.jochef2.campingdiary.data.entities.FullReise;

public class CurrentReiseViewModel extends AndroidViewModel {

    public static ReisenRepository mReisenRepository;
    public static SharedPreferences mPreferences;
    public MutableLiveData<FullReise> mReise = new MutableLiveData<>();

    public CurrentReiseViewModel(@NonNull Application application) {
        super(application);
        mReisenRepository = new ReisenRepository(application);
        mPreferences = application.getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        if (mPreferences.getString("CURRENT_REISE", null) != null) {
            mReisenRepository.getReise(Integer.parseInt(mPreferences.getString("CURRENT_REISE", null))).observeForever(reise -> {
                mReise.setValue(reise);
            });
        } else mReise.setValue(null);
    }
}