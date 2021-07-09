package com.jochef2.campingdiary.ui.route.allRoutes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jochef2.campingdiary.R;

public class AllRoutesFragment extends Fragment {

    private AllRoutesViewModel mViewModel;

    public static AllRoutesFragment newInstance() {
        return new AllRoutesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_routes_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AllRoutesViewModel.class);
        // TODO: Use the ViewModel
    }

}