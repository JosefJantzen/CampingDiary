package com.jochef2.campingdiary.ui.newReise;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.data.ReisenRepository;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.ui.main.CurrentReiseViewModel;

import java.util.Calendar;

public class NewReiseViewModel extends AndroidViewModel {

    public MutableLiveData<Reise> mReise = new MutableLiveData<>();
    private ReisenRepository mReisenRepository;
    private SharedPreferences mPreferences;

    public NewReiseViewModel(Application application) {
        super(application);
        mReise.setValue(new Reise("", Calendar.getInstance()));
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mPreferences = CurrentReiseViewModel.mPreferences;
    }

    public void saveReise() {
        long id = mReisenRepository.insertReise(mReise.getValue());
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("CURRENT_REISE", String.valueOf((int) id));
        editor.apply();
    }
}