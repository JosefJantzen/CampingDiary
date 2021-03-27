package com.jochef2.campingdiary.ui.choosePlace;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.models.Cords;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;
import com.jochef2.campingdiary.data.values.Events;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class ChoosePlaceViewModel extends AndroidViewModel {

    public LiveData<List<com.jochef2.campingdiary.data.entities.Place>> mAllPlaces;

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
    public final PlaceRepository mPlaceRepository;
    public Events mEvent = Events.NIGHT;

    public ChoosePlaceViewModel(@NonNull Application application) {
        super(application);
        mPlaceRepository = new PlaceRepository(application);
        mAllPlaces = mPlaceRepository.getAllShortPlaces();
    }

    /**
     * adds selected place to DB
     *
     * @return id of new place
     */
    @SuppressWarnings("ConstantConditions")
    public int save() {
        if (getField() != FIELDS.NULL) {
            if (getField() == FIELDS.SEARCH) {
                return mSelectedSearchPlaceId.getValue();
            } else {
                if (!getName().isEmpty()) {
                    com.jochef2.campingdiary.data.entities.Place place = new com.jochef2.campingdiary.data.entities.Place(mName.getValue(), new Cords(getCurrentLocation().getValue()));
                    switch (getField()) {
                        case GPS_PREDICTION:
                            Address address = Objects.requireNonNull(mAddressPredictions.getValue()).get(mSelectedGpsPrediction.getValue());
                            com.jochef2.campingdiary.data.models.Address address1 = new com.jochef2.campingdiary.data.models.Address(address);
                            place.setAddressObject(address1);
                            place.setCords(new Cords(address));
                            break;
                        case GOOGLE_PREDICTION:
                            place.setByPlace(Objects.requireNonNull(mPlacePredictions.getValue()).get(mSelectedGooglePrediction.getValue()).getPlace());
                            break;
                        case AUTOCOMPLETE:
                            place.setByPlace(Objects.requireNonNull(mSelectedAutocompletePlace.getValue()));
                            break;
                        case CORDS:
                            break;
                        case MAP:
                            place.setCords(new Cords(Objects.requireNonNull(mSelectedMap.getValue())));
                            break;
                    }
                    return savePlace(place);
                } else {
                    Toast.makeText(getApplication(), getApplication().getString(R.string.err_no_name_place), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplication(), getApplication().getString(R.string.err_no_place_selected), Toast.LENGTH_LONG).show();
        }
        return -1;
    }

    /**
     * insert place into DB
     *
     * @param place to insert
     * @return id of new place
     */
    public int savePlace(com.jochef2.campingdiary.data.entities.Place place) {
        return (int) mPlaceRepository.insertPlace(place);
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

    public Events getEvent() {
        return mEvent;
    }

    public void setEvent(Events event) {
        mEvent = event;
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

    public LiveData<List<com.jochef2.campingdiary.data.entities.Place>> getAllPlaces() {
        return mAllPlaces;
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