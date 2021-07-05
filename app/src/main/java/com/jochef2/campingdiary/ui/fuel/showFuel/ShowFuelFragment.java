package com.jochef2.campingdiary.ui.fuel.showFuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jochef2.campingdiary.R;

public class ShowFuelFragment extends Fragment {

    private ShowFuelViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_fuel_fragment, container, false);
    }
}