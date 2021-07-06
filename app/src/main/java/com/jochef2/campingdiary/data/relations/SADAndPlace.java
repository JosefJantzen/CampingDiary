package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;

public class SADAndPlace {

    @Embedded
    public SupplyAndDisposal mSAD;

    @Relation(
            parentColumn = "placeId",
            entityColumn = "id"
    )
    public Place mPlace;

    public SADAndPlace() {

    }
}
