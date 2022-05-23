package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.Route;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public List<SADAndPlace> mSADs;

    @Relation(
            entity = Fuel.class,
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<FuelAndPlace> mFuels;

    @Relation(
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<Route> mRoutes;


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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * adds all fuel amounts
     * @return two digit double of total fuel
     */
    public double getTotalFuel(){
        double total = 0;
        if (!mFuels.isEmpty()) {
            for (FuelAndPlace fuel : mFuels) {
                total = total + fuel.mFuel.getLiter();
            }
        }
        return round(total, 2);
    }

    /**
     * adds all water amounts
     * @return two digit double of total water
     */
    public double getTotalWater() {
        double total = 0;
        if (!mSADs.isEmpty()) {
            for (SADAndPlace sad : mSADs) {
                total = total + sad.mSAD.getWater();
            }
        }
        return round(total, 2);
    }

    /**
     * adds all prices of every activity
     * TODO: manage currencies
     * @return two digit double of total price
     */
    public double getTotalPrice() {
        double total = 0;
        if (!mNights.isEmpty()) {
            for (NightAndPlace night : mNights) {
                total += night.mNight.getPrice().getPrice();
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
        if (!mSADs.isEmpty()) {
            for (SADAndPlace sad : mSADs) {
                total += sad.mSAD.getPrice().getPrice();
            }
        }
        return round(total, 2);
    }

    public String getAllCountries() {
        List<String> mCountries = new ArrayList<>();
        if (!mNights.isEmpty()) {
            for (NightAndPlace night : mNights) {
                if (night.mPlace != null && night.mPlace.getAddressObject() != null) {
                    if (!mCountries.contains(night.mPlace.getAddressObject().getCountryCode())) {
                        mCountries.add(night.mPlace.getAddressObject().getCountryCode());
                    }
                }
            }
        }
        if (!mEvents.isEmpty()) {
            for (EventAndPlace event : mEvents) {
                if (event.mPlace != null && event.mPlace.getAddressObject() != null) {
                    if (!mCountries.contains(event.mPlace.getAddressObject().getCountryCode())) {
                        mCountries.add(event.mPlace.getAddressObject().getCountryCode());
                    }
                }
            }
        }
        if (!mFuels.isEmpty()) {
            for (FuelAndPlace fuel : mFuels) {
                if (fuel.mPlace != null && fuel.mPlace.getAddressObject() != null) {
                    if (!mCountries.contains(fuel.mPlace.getAddressObject().getCountryCode())) {
                        mCountries.add(fuel.mPlace.getAddressObject().getCountryCode());
                    }
                }
            }
        }
        if (!mSADs.isEmpty()) {
            for (SADAndPlace sad : mSADs) {
                if (sad.mPlace != null && sad.mPlace.getAddressObject() != null) {
                    if (!mCountries.contains(sad.mPlace.getAddressObject().getCountryCode())) {
                        mCountries.add(sad.mPlace.getAddressObject().getCountryCode());
                    }
                }
            }
        }
        StringBuilder string = new StringBuilder();
        if (!mCountries.isEmpty()) {
            for (String country : mCountries) {
                string.append(country + ", ");

            }
            string.deleteCharAt(string.length() -1);
            string.deleteCharAt(string.length() -1);

            if (string.length() > 10) {
                string.delete(10, string.length());
                string.append("...");
            }
        }
        return string.toString();
    }

    public int getTotalDistance() {
        if (mRoutes.size() > 1) {
            return (int) round(mRoutes.get(mRoutes.size() - 1).getMileage() - mRoutes.get(0).getMileage(), 0);
        }
        return 0;
    }
}
