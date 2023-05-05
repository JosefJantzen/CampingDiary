package com.josefjantzen.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.josefjantzen.campingdiary.data.entities.Fuel;
import com.josefjantzen.campingdiary.data.entities.Place;

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
