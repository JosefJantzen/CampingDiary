package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FullReise {

    @Embedded
    public Reise mReise;

    @Relation(
            entity = Night.class,
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<NightAndPlace> mNights;

    @Relation(
            entity = Event.class,
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<EventAndPlace> mEvents;

    @Relation(
            entity = SupplyAndDisposal.class,
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<SADAndPlace> mSuppliesAndDisposals;

    @Relation(
            entity = Fuel.class,
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<FuelAndPlace> mFuels;


    /**
     * searches for last Night
     *
     * @return last Night
     */
    public Night getLastNight() {
        Night lastNight = mNights.get(0).mNight;

        for (int i = 1; i < mNights.size(); i++) {
            Night night = mNights.get(i).mNight;
            if (lastNight.getBegin().before(night.getBegin())) {
                lastNight = night;
            }
        }
        return lastNight;
    }

    public Event getLastEvent() {
        Event lastEvent = mEvents.get(0).mEvent;

        for (int i = 1; i < mEvents.size(); i++) {
            Event event = mEvents.get(i).mEvent;
            if (lastEvent.getBegin().before(event.getBegin())) {
                lastEvent = event;
            }
        }
        return lastEvent;
    }

    /**
     * creates List of Current Night ids
     *
     * @return List of Integer with ids
     */
    public List<Integer> getCurrentNightIds() {
        List<Integer> currentNightIds = new ArrayList<>();
        for (int i = 0; i < mNights.size(); i++) {
            Night night = mNights.get(i).mNight;
            if (night.getBegin().before(Calendar.getInstance()) && night.getEnd().after(Calendar.getInstance())) {
                currentNightIds.add(i);
            }
        }
        return currentNightIds;
    }

    /**
     * adds all fuel amounts
     * @return double of total fuel
     */
    public double getTotalFuel(){
        double total = 0;
        if (!mFuels.isEmpty()) {
            for (FuelAndPlace fuel : mFuels) {
                total = total + fuel.mFuel.getLiter();
            }
        }
        return total;
    }

    /**
     * adds all prices of every activity
     * TODO: manage currencies
     * @return
     */
    public double getTotalPrice() {
        double total = 0;
        if (!mNights.isEmpty()) {
            for (NightAndPlace night : mNights) {
                total = total + night.mNight.getPrice().getPrice();
            }
        }
        if (!mEvents.isEmpty()) {
            for (EventAndPlace event : mEvents) {
                total += event.mEvent.getPrice().getPrice();
            }
        }
        if (!mFuels.isEmpty()) {
            for (FuelAndPlace fuel : mFuels) {
                total += fuel.mFuel.getPrice().getPrice();
            }
        }
        if (!mSuppliesAndDisposals.isEmpty()) {
            for (SADAndPlace sad : mSuppliesAndDisposals) {
                total += sad.mSAD.getPrice().getPrice();
            }
        }
        return total;
    }
}
