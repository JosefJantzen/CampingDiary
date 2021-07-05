package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Place;

public class EventAndPlace {

    @Embedded
    public Event mEvent;

    @Relation(
            parentColumn = "placeId",
            entityColumn = "id"
    )
    public Place mPlace;

    public EventAndPlace(Event event) {
        mEvent = event;
    }
}
