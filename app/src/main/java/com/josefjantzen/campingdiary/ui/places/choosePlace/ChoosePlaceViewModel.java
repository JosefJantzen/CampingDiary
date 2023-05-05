package com.josefjantzen.campingdiary.ui.places.choosePlace;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.models.Cords;
import com.josefjantzen.campingdiary.data.relations.FullPlace;
import com.josefjantzen.campingdiary.data.repositories.PlaceRepository;
import com.josefjantzen.campingdiary.data.values.Events;
import com.josefjantzen.campingdiary.data.values.PlaceSortBy;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class ChoosePlaceViewModel extends AndroidViewModel {

    public LiveData<List<FullPlace>> mAllPlaces;

    public MutableLiveData<Location> mCurrentLocation = new MutableLiveData<>();
    public MutableLiveData<List<Address>> mAddressPredictions = new MutableLiveData<>();
    public MutableLiveData<List<PlaceLikelihood>> mPlacePredictions = new MutableLiveData<>();

    public MutableLiveData<FIELDS> mField = new MutableLiveData<>(FIELDS.NULL);

    public MutableLiveData<Place> mSelectedAutocompletePlace = new MutableLiveData<>();
    public MutableLiveData<Integer> mSelectedGpsPrediction = new MutableLiveData<>(-1);
    public MutableLiveData<Integer> mSelectedGooglePrediction = new MutableLiveData<>(-1);
    public MutableLiveData<PointOfInterest> mSelectedMap = new MutableLiveData<>();

    public MutableLiveData<Integer> mSelectedSearchPlaceId = new MutableLiveData<>(-1);

    public MutableLiveData<String> mName = new MutableLiveData<>("");
    public static PlaceRepository mPlaceRepository;
    public Events mEvent = Events.NIGHT;

    public MutableLiveData<PlaceSortBy> mPlaceSortBy = new MutableLiveData<>(PlaceSortBy.NAME);
    public MutableLiveData<String> mSearchQuery = new MutableLiveData<>("");
    public MutableLiveData<List<FullPlace>> mShownPlaces = new MutableLiveData<>();

    public ChoosePlaceViewModel(@NonNull Application application) {
        super(application);
        mPlaceRepository = new PlaceRepository(application);
        mAllPlaces = mPlaceRepository.getAllPlaces();
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
                    com.josefjantzen.campingdiary.data.entities.Place place = new com.josefjantzen.campingdiary.data.entities.Place(mName.getValue(), new Cords(getCurrentLocation().getValue()));
                    switch (getField()) {
                        case GPS_PREDICTION:
                            Address address = Objects.requireNonNull(mAddressPredictions.getValue()).get(mSelectedGpsPrediction.getValue());
                            com.josefjantzen.campingdiary.data.models.Address address1 = new com.josefjantzen.campingdiary.data.models.Address(address);
                            place.setAddressObject(address1);
                            place.setCords(new Cords(address));
                            break;
                        case GOOGLE_PREDICTION:
                            place.setByPlace(Objects.requireNonNull(mPlacePredictions.getValue()).get(mSelectedGooglePrediction.getValue()).getPlace(), getApplication().getApplicationContext());
                            break;
                        case AUTOCOMPLETE:
                            place.setByPlace(Objects.requireNonNull(mSelectedAutocompletePlace.getValue()), getApplication().getApplicationContext());
                            break;
                        case CORDS:
                            place.predictAddress(getApplication().getApplicationContext());
                            break;
                        case MAP:
                            PointOfInterest poi = mSelectedMap.getValue();
                            if (poi.name == null || poi.placeId == null) {
                                place.setPlaceId(poi.placeId);
                            }
                            place.setCords(new Cords(poi.latLng));
                            place.predictAddress(getApplication().getApplicationContext());
                            break;
                    }
                    place.generateAddressString();
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
    public int savePlace(com.josefjantzen.campingdiary.data.entities.Place place) {
        return (int) mPlaceRepository.insertPlace(place);
    }

    public void updatePlaces(List<com.josefjantzen.campingdiary.data.entities.Place> places) {
        mPlaceRepository.updatePlace(places);
    }

    public MutableLiveData<Location> getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        mCurrentLocation.setValue(currentLocation);
        if (currentLocation != null) {
            setSelectedMap(new PointOfInterest(new LatLng(mCurrentLocation.getValue().getLatitude(), mCurrentLocation.getValue().getLongitude()), "", ""));
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

    public MutableLiveData<PointOfInterest> getSelectedMap() {
        return mSelectedMap;
    }

    public void setSelectedMap(PointOfInterest selectedMap) {
        mSelectedMap.setValue(selectedMap);
    }

    public LiveData<List<FullPlace>> getAllPlaces() {
        return mAllPlaces;
    }

    public void setSelectedSearchPlaceId(Integer selectedSearchPlaceId) {
        mSelectedSearchPlaceId.setValue(selectedSearchPlaceId);
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