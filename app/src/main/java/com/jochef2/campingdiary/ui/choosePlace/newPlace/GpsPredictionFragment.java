package com.jochef2.campingdiary.ui.choosePlace.newPlace;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsPredictionFragment extends Fragment {

    private ChoosePlaceViewModel mViewModel;

    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps_prediction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);

        mRecyclerView = view.findViewById(R.id.recycler);

        mViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                Geocoder geocoder = new Geocoder(requireActivity().getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

                    List<Address> addresses1 = geocoder.getFromLocationName("Langenbeker Friedhofsweg 5, 21079 Hamburg, Deutschland", 5);
                    for (Address address : addresses1) {
                        Log.d("TAG", address.getAddressLine(0));
                    }

                    GpsPredictionsAdapter gpsPredictionsAdapter = new GpsPredictionsAdapter(addresses);
                    mRecyclerView.setAdapter(gpsPredictionsAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireActivity(), "no location", Toast.LENGTH_SHORT).show();
            }
        });

    }
}