package com.jochef2.campingdiary.ui.places.choosePlace.searchPlace;

import java.util.Calendar;
import java.util.List;

public class Filter {

    private float mMinDistance;
    private float mMaxDistance;
    private List<Float> mRangeDistanceValues;

    private Calendar mLastVisited;

    private Visit mVisit;


    public enum Visit {
        TRUE,
        FALSE,
        BOTH
    }
}
