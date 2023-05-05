package com.josefjantzen.campingdiary.ui.sad.newSad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.entities.SupplyAndDisposal;
import com.josefjantzen.campingdiary.data.models.Price;
import com.josefjantzen.campingdiary.data.relations.FullPlace;
import com.josefjantzen.campingdiary.data.repositories.PlaceRepository;
import com.josefjantzen.campingdiary.data.repositories.ReisenRepository;
import com.josefjantzen.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class NewSADViewModel extends AndroidViewModel {

    private final PlaceRepository mPlaceRepository;
    private final ReisenRepository mReisenRepository;
    public MutableLiveData<SupplyAndDisposal> mSAD = new MutableLiveData<>();
    public int reiseId;
    public MutableLiveData<FullPlace> mPlace = new MutableLiveData<>();

    public NewSADViewModel(@NonNull @NotNull Application application) {
        super(application);

        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mPlaceRepository = new PlaceRepository(application);

        SupplyAndDisposal sad = new SupplyAndDisposal(-1 , application.getString(R.string.no_name), Calendar.getInstance(), new Price(0, ExtendedCurrency.getCurrencyByISO("EUR")));
        mSAD.setValue(sad);
    }

    public void setReiseId(int reiseId) {
        SupplyAndDisposal sad = mSAD.getValue();
        sad.setReiseId(reiseId);
        mSAD.setValue(sad);
    }

    public void saveSAD() {
        mReisenRepository.insertSupplyAndDisposal(mSAD.getValue());
    }

    public MutableLiveData<FullPlace> getPlace() {
        return mPlace;
    }

    public void setPlace(int placeId) {
        SupplyAndDisposal sad = mSAD.getValue();
        sad.setPlaceId(placeId);
        mSAD.setValue(sad);
        mPlaceRepository.getPlace(placeId).observeForever(place -> {
            mPlace.setValue(place);
        });
    }
}