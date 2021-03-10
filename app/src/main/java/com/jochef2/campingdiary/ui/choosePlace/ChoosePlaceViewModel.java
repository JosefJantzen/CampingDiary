package com.jochef2.campingdiary.ui.choosePlace;

import android.app.Application;
import android.location.Address;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;

import java.util.List;

public class ChoosePlaceViewModel extends AndroidViewModel {

    public MutableLiveData<Location> mCurrentLocation = new MutableLiveData<>();
    public MutableLiveData<List<Address>> mAddressPredictions = new MutableLiveData<>();
    public MutableLiveData<List<PlaceLikelihood>> mPlacePredictions = new MutableLiveData<>();
    public String mName;
    public MutableLiveData<Place> mPlace = new MutableLiveData<>();
    private PlaceRepository mPlaceRepository;

    public ChoosePlaceViewModel(@NonNull Application application) {
        super(application);
        // TODO: mPlaceRepository = new PlaceRepository(application);
        mPlace.setValue(new Place());
    }

    public MutableLiveData<Location> getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        mCurrentLocation.setValue(currentLocation);
    }
}