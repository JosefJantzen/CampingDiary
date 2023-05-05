package com.josefjantzen.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.josefjantzen.campingdiary.data.entities.Place;
import com.josefjantzen.campingdiary.data.entities.SupplyAndDisposal;

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
