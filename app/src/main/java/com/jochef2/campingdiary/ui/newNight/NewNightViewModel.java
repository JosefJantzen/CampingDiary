package com.jochef2.campingdiary.ui.newNight;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.ReisenRepository;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.values.NightCategory;
import com.jochef2.campingdiary.ui.main.CurrentReiseViewModel;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.Calendar;

public class NewNightViewModel extends AndroidViewModel {

    public MutableLiveData<Night> mNight = new MutableLiveData<>();
    public int reiseId;
    public int lastChip;
    private ReisenRepository mReisenRepository;

    public NewNightViewModel(@NonNull Application application) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 1);
        Night night = new Night(-1, application.getString(R.string.no_name), Calendar.getInstance(), c, NightCategory.MOTORHOME_AREA, new Price(0, ExtendedCurrency.getCurrencyByISO("EUR")));
        mNight.setValue(night);
        lastChip = R.id.ch_one;
    }

    public void setReiseId(int reiseId) {
        Night night = mNight.getValue();
        night.setReiseId(reiseId);
        mNight.setValue(night);
    }

    public void saveNight() {
        mReisenRepository.insertNight(mNight.getValue());
    }
}