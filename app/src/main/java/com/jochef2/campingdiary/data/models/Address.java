package com.jochef2.campingdiary.data.models;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Address {

    public String mStreetWithNumber;
    public String mPostalCodeAndTown;
    public String mCity;
    public String mState;
    public String mCountry;

    public Address(String streetWithNumber, String postalCodeAndTown, String country) {
        mStreetWithNumber = streetWithNumber;
        mPostalCodeAndTown = postalCodeAndTown;
        mCountry = country;
    }

    public Address(String addressString) {
        List<String> parts = Arrays.asList(addressString.split(","));
        mStreetWithNumber = parts.get(0);
        mPostalCodeAndTown = parts.get(1);
        mCountry = parts.get(2);
    }

    @NotNull
    public String toString() {
        return this.mStreetWithNumber + "," + this.mPostalCodeAndTown + "," + this.mCountry;
    }

    public Address convert(String address) {
        List<String> parts = Arrays.asList(address.split(","));
        return new Address(parts.get(0), parts.get(1), parts.get(2));
    }
}
