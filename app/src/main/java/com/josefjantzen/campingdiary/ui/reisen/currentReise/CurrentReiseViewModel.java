package com.josefjantzen.campingdiary.ui.reisen.currentReise;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.josefjantzen.campingdiary.data.relations.FullReise;
import com.josefjantzen.campingdiary.data.repositories.ReisenRepository;

import java.util.Calendar;

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
        mReisenRepository.getCurrentReise().observeForever(fullReise -> {
            if (fullReise != null && fullReise.mReise.getBegin().before(Calendar.getInstance()) && fullReise.mReise.getEnd().after(Calendar.getInstance())) {
                mReise.setValue(fullReise);
            } else mReise.setValue(null);
        });
    }
}