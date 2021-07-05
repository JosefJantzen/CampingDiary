package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Place;

public class FuelAndPlace {

    @Embedded
    public Fuel mFuel;

    @Relation(
            parentColumn = "placeId",
            entityColumn = "id"
    )
    public Place mPlace;

    public FuelAndPlace(Fuel fuel) {
        mFuel = fuel;
    }
}
