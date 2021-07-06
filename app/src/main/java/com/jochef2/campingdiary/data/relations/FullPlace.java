package com.jochef2.campingdiary.data.relations;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.entities.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FullPlace implements SortedListAdapter.ViewModel {

    @Embedded
    public Place mPlace;

    @Relation(
            parentColumn = "id",
            entityColumn = "placeId"
    )
    public List<Night> mNights;

    @Relation(
            parentColumn = "id",
            entityColumn = "placeId"
    )
    public List<Event> mEvents;

    @Relation(
            parentColumn = "id",
            entityColumn = "placeId"
    )
    public List<Fuel> mFuels;

    /**
     * counts number of all events with this FullPlace
     *
     * @return Integer with that number
     */
    public int getNumberOfVisits() {
        return mNights.size() + mEvents.size(); //TODO: add sizes of other event types
    }

    /**
     * searches for the last Event in a FullPlace
     *
     * @return Millis of last Event End
     */
    public Long getLastEventDateInMillis() {
        List<Long> lasts = new ArrayList<>();
        if (!mNights.isEmpty()) {
            Comparator<Night> nightComparator = (o1, o2) -> Long.compare(o1.getEnd().getTimeInMillis(), o2.getEnd().getTimeInMillis());
            Collections.sort(mNights, nightComparator);
            lasts.add(mNights.get(0).getEnd().getTimeInMillis());
        }
        if (!mEvents.isEmpty()) {
            Comparator<Event> eventComparator = ((o1, o2) -> Long.compare(o1.getEnd().getTimeInMillis(), o2.getEnd().getTimeInMillis()));
            Collections.sort(mEvents, eventComparator);
            lasts.add(mEvents.get(0).getEnd().getTimeInMillis());
        }
        if (!mFuels.isEmpty()) {
            Comparator<Fuel> fuelComparator = ((o1, o2) -> Long.compare(o1.getTime().getTimeInMillis(), o2.getTime().getTimeInMillis()));
            Collections.sort(mFuels, fuelComparator);
            lasts.add(mFuels.get(0).getTime().getTimeInMillis());
        }
        //TODO: add Comparators for other event types
        if (!lasts.isEmpty()) {
            Collections.sort(lasts);
            return lasts.get(0);
        } else {
            return 0L;
        }
    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T model) {
        if (model instanceof FullPlace) {
            final FullPlace place = (FullPlace) model;
            return place.mPlace.mId == mPlace.mId;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T model) {
        if (model instanceof FullPlace) {
            final FullPlace other = (FullPlace) model;
            if (mPlace.mId != other.mPlace.mId) {
                return false;
            }
            return Objects.equals(mPlace.mPlaceName, other.mPlace.mPlaceName);
        }
        return false;
    }
}
