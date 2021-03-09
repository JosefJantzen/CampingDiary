package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Place;

import java.util.List;

public class FullPlace {

    @Embedded
    public Place mPlace;

    @Relation(
            parentColumn = "id",
            entityColumn = "placeId"
    )
    public List<Night> mNights;
}
