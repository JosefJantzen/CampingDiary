package com.jochef2.campingdiary.ui.choosePlace;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.models.Cords;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;

import java.util.List;

public class ChoosePlaceViewModel extends AndroidViewModel {

    public MutableLiveData<Location> mCurrentLocation = new MutableLiveData<>();
    public MutableLiveData<List<Address>> mAddressPredictions = new MutableLiveData<>();
    public MutableLiveData<List<PlaceLikelihood>> mPlacePredictions = new MutableLiveData<>();

    public MutableLiveData<FIELDS> mField = new MutableLiveData<>(FIELDS.NULL);

    public MutableLiveData<Place> mSelectedAutocompletePlace = new MutableLiveData<>();
    public MutableLiveData<Integer> mSelectedGpsPrediction = new MutableLiveData<>(-1);
    public MutableLiveData<Integer> mSelectedGooglePrediction = new MutableLiveData<>(-1);
    public MutableLiveData<LatLng> mSelectedMap = new MutableLiveData<>();

    public MutableLiveData<Integer> mSelectedSearchPlaceId = new MutableLiveData<>(-1);

    public MutableLiveData<String> mName = new MutableLiveData<>("");
    //public MutableLiveData<Place> mPlace = new MutableLiveData<>();
    private PlaceRepository mPlaceRepository;

    public ChoosePlaceViewModel(@NonNull Application application) {
        super(application);
        // TODO: mPlaceRepository = new PlaceRepository(application);
    }

    public void save() {
        if (getField() != FIELDS.NULL) {
            if (getField() == FIELDS.SEARCH) {
                //TODO: implement search
            } else {
                if (!getName().isEmpty()) {
                    com.jochef2.campingdiary.data.entities.Place place = new com.jochef2.campingdiary.data.entities.Place(mName.getValue(), new Cords(getCurrentLocation().getValue()));
                    switch (getField()) {
                        case GPS_PREDICTION:
                            Address address = mAddressPredictions.getValue().get(mSelectedGpsPrediction.getValue());
                            place.setAddressObject(address);
                            break;
                        case GOOGLE_PREDICTION:
                            place.setByPlace(mPlacePredictions.getValue().get(mSelectedGooglePrediction.getValue()).getPlace());
                            break;
                        case AUTOCOMPLETE:
                            place.setByPlace(mSelectedAutocompletePlace.getValue());
                            break;
                        case CORDS:
                            break;
                        case MAP:
                            place.setCords(new Cords(mSelectedMap.getValue()));
                            break;
                    }
                } else {
                    Toast.makeText(getApplication(), getApplication().getString(R.string.err_no_name_place), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplication(), getApplication().getString(R.string.err_no_place_selected), Toast.LENGTH_LONG).show();
        }
    }

    public void savePlace() {
        //TODO: save Place to Database and return id
    }

    public MutableLiveData<Location> getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        mCurrentLocation.setValue(currentLocation);
        if (currentLocation != null) {
            setSelectedMap(new LatLng(mCurrentLocation.getValue().getLatitude(), mCurrentLocation.getValue().getLongitude()));
        }
    }

    public MutableLiveData<List<PlaceLikelihood>> getPlacePredictions() {
        return mPlacePredictions;
    }

    public void setPlacePredictions(List<PlaceLikelihood> placePredictions) {
        mPlacePredictions.setValue(placePredictions);
    }

    public String getName() {
        return mName.getValue();
    }

    public void setName(String name) {
        mName.setValue(name);
    }

    public MutableLiveData<List<Address>> getAddressPredictions() {
        return mAddressPredictions;
    }

    public void setAddressPredictions(List<Address> addressPredictions) {
        mAddressPredictions.setValue(addressPredictions);
    }

    public FIELDS getField() {
        return mField.getValue();
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

    public int getSelectedGpsPrediction() {
        return mSelectedGpsPrediction.getValue();
    }

    public void setSelectedGpsPrediction(int selectedGpsPrediction) {
        mSelectedGpsPrediction.setValue(selectedGpsPrediction);
    }

    public int getSelectedGooglePrediction() {
        return mSelectedGooglePrediction.getValue();
    }

    public void setSelectedGooglePrediction(int selectedGooglePrediction) {
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
        SEARCH,
        GPS_PREDICTION,
        GOOGLE_PREDICTION,
        AUTOCOMPLETE,
        CORDS,
        MAP
    }
}