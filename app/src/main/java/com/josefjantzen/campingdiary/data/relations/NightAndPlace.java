package com.josefjantzen.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.josefjantzen.campingdiary.data.entities.Night;
import com.josefjantzen.campingdiary.data.entities.Place;


public class NightAndPlace {

    @Embedded
    public Night mNight;

    @Relation(
            parentColumn = "placeId",
            entityColumn = "id"
    )
    public Place mPlace;

    @Ignore
    public NightAndPlace(Night night, Place place) {
        mNight = night;
        mPlace = place;
    }

    public NightAndPlace(Night night) {
        mNight = night;
    }
}
