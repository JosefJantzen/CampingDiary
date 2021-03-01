package com.jochef2.campingdiary.data.models;

import androidx.room.ColumnInfo;

import com.mynameismidori.currencypicker.ExtendedCurrency;

public class Price {

    @ColumnInfo(name = "price")
    public double mPrice;

    @ColumnInfo(name = "currency")
    public ExtendedCurrency mCurrency;

    public Price(double price, ExtendedCurrency currency) {
        mPrice = price;
        mCurrency = currency;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public ExtendedCurrency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(ExtendedCurrency currency) {
        mCurrency = currency;
    }

    public void setCurrency(String ISO) {
        mCurrency = ExtendedCurrency.getCurrencyByISO(ISO);
    }
}
