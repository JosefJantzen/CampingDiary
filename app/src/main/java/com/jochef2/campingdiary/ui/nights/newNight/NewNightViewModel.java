package com.jochef2.campingdiary.ui.nights.newNight;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.relations.FullPlace;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.data.values.NightCategory;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.Calendar;
import java.util.Objects;

public class NewNightViewModel extends AndroidViewModel {

    public MutableLiveData<Night> mNight = new MutableLiveData<>();
    public int reiseId;
    public int lastStartChip;
    public int lastEndChip;
    private final PlaceRepository mPlaceRepository;
    private final ReisenRepository mReisenRepository;
    public MutableLiveData<FullPlace> mPlace = new MutableLiveData<>();

    public NewNightViewModel(@NonNull Application application) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mPlaceRepository = new PlaceRepository(application);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Night night = new Night(-1, application.getString(R.string.no_name), Calendar.getInstance(), c, NightCategory.MOTORHOME_AREA, new Price(0, ExtendedCurrency.getCurrencyByISO("EUR")));
        mNight.setValue(night);
        lastEndChip = R.id.ch_one;
        lastStartChip = R.id.ch_start_today;
    }

    public void setReiseId(int reiseId) {
        Night night = mNight.getValue();
        Objects.requireNonNull(night).setReiseId(reiseId);
        mNight.setValue(night);
    }

    public void saveNight() {
        mReisenRepository.insertNight(mNight.getValue());
    }

    public MutableLiveData<FullPlace> getPlace() {
        return mPlace;
    }

    public void setPlace(int placeId) {
        Night night = mNight.getValue();
        night.setPlaceId(placeId);
        mNight.setValue(night);
        mPlaceRepository.getPlace(placeId).observeForever(place -> {
            mPlace.setValue(place);
        });
    }
}