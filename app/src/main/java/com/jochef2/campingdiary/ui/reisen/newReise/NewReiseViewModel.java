package com.jochef2.campingdiary.ui.reisen.newReise;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

import java.util.Calendar;

public class NewReiseViewModel extends AndroidViewModel {

    public MutableLiveData<Reise> mReise = new MutableLiveData<>();
    private final ReisenRepository mReisenRepository;
    private final SharedPreferences mPreferences;
    public int lastChip;

    /**
     * creates new reise with current date as start and day in two weeks as end
     * initial mReisenRepository and mPreferences
     *
     * @param application current application used for Context
     */
    public NewReiseViewModel(Application application) {
        super(application);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 14);
        mReise.setValue(new Reise("", Calendar.getInstance(), c));
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mPreferences = CurrentReiseViewModel.mPreferences;
        lastChip = R.id.ch_two;
    }

    /**
     * saves reise in db
     * saves id of new current reise in preferences
     */
    public void saveReise() {
        long id = mReisenRepository.insertReise(mReise.getValue());
    }
}