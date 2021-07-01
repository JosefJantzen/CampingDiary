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
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<Event> mEvents;

    @Relation(
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<SupplyAndDisposal> mSuppliesAndDisposals;

    @Relation(
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<Fuel> mFuels;


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
}
