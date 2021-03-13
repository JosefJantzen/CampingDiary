package com.jochef2.campingdiary.utils;

import android.location.Address;

import java.util.ArrayList;
import java.util.List;

public class Convert {

    public static List<Address> removeCountries(List<Address> addresses) {
        List<Address> newAddresses = new ArrayList<>();
        for (int i = 0; i < addresses.size(); i++) {
            String[] parts = addresses.get(i).getAddressLine(0).split(",");
            Address address = addresses.get(i);
            address.setAddressLine(0, parts[0] + "," + parts[1]);
        }
        return newAddresses;
    }
}
