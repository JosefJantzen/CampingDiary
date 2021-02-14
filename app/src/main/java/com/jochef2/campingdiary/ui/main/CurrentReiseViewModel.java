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

    /**
     * Either set's mReise to CurrentReise or empty FullReise = {}
     * initial mReisenRepository and mPreferences
     *
     * @param application current application used for Context
     */
    public CurrentReiseViewModel(@NonNull Application application) {
        super(application);
        mReisenRepository = new ReisenRepository(application);
        mPreferences = application.getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        if (!mPreferences.getString("CURRENT_REISE", "-1").equals("-1")) {
            mReisenRepository.getReise(Integer.parseInt(mPreferences.getString("CURRENT_REISE", "-1"))).observeForever(reise -> {
                mReise.setValue(reise);
            });
        } else mReise.setValue(new FullReise());
    }

    /**
     * removes the current reise id and sets it to -1
     */
    public void clearCurrent() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("CURRENT_REISE", "-1");
        editor.apply();
    }
}