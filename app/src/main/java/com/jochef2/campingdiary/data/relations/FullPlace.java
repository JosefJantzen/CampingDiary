package com.jochef2.campingdiary.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FullPlace {

    @Embedded
    public Place mPlace;

    @Relation(
            parentColumn = "id",
            entityColumn = "placeId"
    )
    public List<Night> mNights;

    public int getNumberOfVisits() {
        return mNights.size(); //TODO: add sizes of other event types
    }

    public Long getLastEventDateInMillis() {
        List<Long> lasts = new ArrayList<>();

        Comparator<Night> nightComparator = (o1, o2) -> Long.compare(o1.getEnd().getTimeInMillis(), o2.getEnd().getTimeInMillis());
        Collections.sort(mNights, nightComparator);
        lasts.add(mNights.get(0).getEnd().getTimeInMillis());

        //TODO: add Comparators for other event types

        Collections.sort(lasts);
        return lasts.get(0);
    }
}
