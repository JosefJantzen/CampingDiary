package com.jochef2.campingdiary.ui.fuel.newFuel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.relations.FullPlace;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewFuelViewModel extends AndroidViewModel {

    private final PlaceRepository mPlaceRepository;
    private final ReisenRepository mReisenRepository;
    public MutableLiveData<Fuel> mFuel = new MutableLiveData<>();
    public int reiseId;
    public MutableLiveData<FullPlace> mPlace = new MutableLiveData<>();

    public NewFuelViewModel(@NonNull @NotNull Application application) {
        super(application);

        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mPlaceRepository = new PlaceRepository(application);

        Fuel fuel = new Fuel(-1, application.getString(R.string.no_name), Calendar.getInstance(), new Price(-1, ExtendedCurrency.getCurrencyByISO("EUR")));
        mFuel.setValue(fuel);
    }

    public void setReiseId(int reiseId) {
        Fuel fuel = mFuel.getValue();
        Objects.requireNonNull(fuel).setReiseId(reiseId);
        mFuel.setValue(fuel);
    }

    public void saveFuel() {
        mReisenRepository.insertFuel(mFuel.getValue());
    }

    public MutableLiveData<FullPlace> getPlace() {
        return mPlace;
    }

    public void setPlace(int placeId) {
        Fuel fuel = mFuel.getValue();
        fuel.setPlaceId(placeId);
        mFuel.setValue(fuel);
        mPlaceRepository.getPlace(placeId).observeForever(place -> {
            mPlace.setValue(place);
        });
    }
}