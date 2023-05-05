package com.josefjantzen.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.josefjantzen.campingdiary.data.entities.Event;
import com.josefjantzen.campingdiary.data.entities.Place;

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
