package com.jochef2.campingdiary.ui.sad.newSad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;
import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.relations.FullPlace;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;
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

        SupplyAndDisposal sad = new SupplyAndDisposal(-1 , application.getString(R.string.no_name), Calendar.getInstance(), new Price(-1, ExtendedCurrency.getCurrencyByISO("EUR")));
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