package com.jochef2.campingdiary.ui.prices.allPrices;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jochef2.campingdiary.R;

public class AllPricesFragment extends Fragment {

    private AllPricesViewModel mViewModel;

    public static AllPricesFragment newInstance() {
        return new AllPricesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_prices, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AllPricesViewModel.class);
        // TODO: Use the ViewModel
    }

}