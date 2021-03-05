package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Reise;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

import java.util.List;

public class FullReise {

    @Embedded
    public Reise mReise;

    @Relation(
            parentColumn = "id",
            entityColumn = "reiseId"
    )
    public List<Night> mNights;

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
}
