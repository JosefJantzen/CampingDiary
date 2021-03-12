package com.jochef2.campingdiary.ui.choosePlace;

import android.app.Application;
import android.location.Address;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;

import java.util.List;

public class ChoosePlaceViewModel extends AndroidViewModel {

    public MutableLiveData<Location> mCurrentLocation = new MutableLiveData<>();
    public MutableLiveData<List<Address>> mAddressPredictions = new MutableLiveData<>();
    public MutableLiveData<List<PlaceLikelihood>> mPlacePredictions = new MutableLiveData<>();

    public MutableLiveData<FIELDS> mField = new MutableLiveData<>();

    public MutableLiveData<Place> mSelectedAutocompletePlace = new MutableLiveData<>();
    public MutableLiveData<String> mSelectedGpsPrediction = new MutableLiveData<>();
    public MutableLiveData<Place> mSelectedGooglePrediction = new MutableLiveData<>();
    public MutableLiveData<LatLng> mSelectedMap = new MutableLiveData<>();

    public String mName;
    //public MutableLiveData<Place> mPlace = new MutableLiveData<>();
    private PlaceRepository mPlaceRepository;

    public ChoosePlaceViewModel(@NonNull Application application) {
        super(application);
        // TODO: mPlaceRepository = new PlaceRepository(application);
        //mPlace.setValue(new Place());
    }

    public MutableLiveData<Location> getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        mCurrentLocation.setValue(currentLocation);
        setSelectedMap(new LatLng(mCurrentLocation.getValue().getLatitude(), mCurrentLocation.getValue().getLongitude()));
    }

    public MutableLiveData<List<PlaceLikelihood>> getPlacePredictions() {
        return mPlacePredictions;
    }

    public void setPlacePredictions(List<PlaceLikelihood> placePredictions) {
        mPlacePredictions.setValue(placePredictions);
    }

    public MutableLiveData<List<Address>> getAddressPredictions() {
        return mAddressPredictions;
    }

    public void setAddressPredictions(List<Address> addressPredictions) {
        mAddressPredictions.setValue(addressPredictions);
    }

    public MutableLiveData<FIELDS> getField() {
        return mField;
    }

    public void setField(FIELDS field) {
        mField.setValue(field);
    }

    public MutableLiveData<Place> getSelectedAutocompletePlace() {
        return mSelectedAutocompletePlace;
    }

    public void setSelectedAutocompletePlace(Place selectedAutocompletePlace) {
        mSelectedAutocompletePlace.setValue(selectedAutocompletePlace);
    }

    public MutableLiveData<String> getSelectedGpsPrediction() {
        return mSelectedGpsPrediction;
    }

    public void setSelectedGpsPrediction(String selectedGpsPrediction) {
        mSelectedGpsPrediction.setValue(selectedGpsPrediction);
    }

    public MutableLiveData<Place> getSelectedGooglePrediction() {
        return mSelectedGooglePrediction;
    }

    public void setSelectedGooglePrediction(Place selectedGooglePrediction) {
        mSelectedGooglePrediction.setValue(selectedGooglePrediction);
    }

    public MutableLiveData<LatLng> getSelectedMap() {
        return mSelectedMap;
    }

    public void setSelectedMap(LatLng selectedMap) {
        mSelectedMap.setValue(selectedMap);
    }

    public enum FIELDS {
        NULL,
        GPS_PREDICTION,
        GOOGLE_PREDICTION,
        AUTOCOMPLETE,
        CORDS,
        MAP
    }
}