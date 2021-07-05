package com.jochef2.campingdiary.ui.fuel.newFuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jochef2.campingdiary.R;

public class NewFuelFragment extends Fragment {

    private NewFuelViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_fuel_fragment, container, false);
    }
}