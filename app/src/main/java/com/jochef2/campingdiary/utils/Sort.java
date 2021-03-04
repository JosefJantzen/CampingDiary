package com.jochef2.campingdiary.utils;

import com.jochef2.campingdiary.data.entities.Night;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Sort {

    public static Night getLastNight(List<Night> nights) {
        Night lastNight = nights.get(0);

        for (int i = 1; i < nights.size(); i++) {
            Night night = nights.get(i);
            if (lastNight.getBegin().before(night.getBegin())) {
                lastNight = night;
            }
        }
        return lastNight;
    }

    public static List<Integer> getCurrentNightIds(List<Night> nights) {
        List<Integer> currentNightIds = new ArrayList<>();
        for (int i = 0; i < nights.size(); i++) {
            Night night = nights.get(i);
            if (night.getBegin().before(Calendar.getInstance()) && night.getEnd().after(Calendar.getInstance())) {
                currentNightIds.add(i);
            }
        }
        return currentNightIds;
    }
}
