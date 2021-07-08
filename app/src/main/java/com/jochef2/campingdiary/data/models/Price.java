package com.jochef2.campingdiary.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.jetbrains.annotations.NotNull;

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
        if (mPrice == -1) {
            return 0;
        }
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

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return mPrice + mCurrency.getSymbol();
    }
}
