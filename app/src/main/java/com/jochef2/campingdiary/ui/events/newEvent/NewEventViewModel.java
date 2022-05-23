package com.jochef2.campingdiary.ui.events.newEvent;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.models.Price;
import com.jochef2.campingdiary.data.relations.FullPlace;
import com.jochef2.campingdiary.data.repositories.PlaceRepository;
import com.jochef2.campingdiary.data.repositories.ReisenRepository;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewEventViewModel extends AndroidViewModel {

    private final PlaceRepository mPlaceRepository;
    private final ReisenRepository mReisenRepository;
    public MutableLiveData<Event> mEvent = new MutableLiveData<>();
    public int mReiseId;
    public int lastStartDayChip = R.id.ch_start_today;
    public int lastStartTimeChip = R.id.ch_start_one;
    public int lastEndDayChip = R.id.ch_end_today;
    public int lastEndTimeChip = R.id.ch_end_two;
    public MutableLiveData<FullPlace> mPlace = new MutableLiveData<>();

    public NewEventViewModel(@NonNull @NotNull Application application) {
        super(application);
        mReisenRepository = CurrentReiseViewModel.mReisenRepository;
        mPlaceRepository = new PlaceRepository(application);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 2);
        Event event = new Event(-1, application.getString(R.string.no_name), Calendar.getInstance(), c, new Price(0, ExtendedCurrency.getCurrencyByISO("EUR")));
        mEvent.setValue(event);
    }

    public void setReiseId(int reiseId) {
        mReiseId = reiseId;
        Event event = mEvent.getValue();
        Objects.requireNonNull(event).setReiseId(reiseId);
        mEvent.setValue(event);
    }

    public void saveEvent() {
        mReisenRepository.insertEvent(mEvent.getValue());
    }

    public void setPlace(int placeId) {
        Event event = mEvent.getValue();
        event.setPlaceId(placeId);
        mEvent.setValue(event);
        mPlaceRepository.getPlace(placeId).observeForever(place -> {
            mPlace.setValue(place);
        });
    }
}