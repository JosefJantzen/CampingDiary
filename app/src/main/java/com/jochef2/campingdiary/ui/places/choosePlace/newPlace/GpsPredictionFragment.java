package com.jochef2.campingdiary.ui.places.choosePlace.newPlace;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.jochef2.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsPredictionFragment extends Fragment {

    private static ChoosePlaceViewModel mViewModel;

    private static RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps_prediction, container, false);
    }

    /**
     * unselects the the selected card in recycler
     */
    public static void unselect() {
        GpsPredictionsAdapter adapter = (GpsPredictionsAdapter) mRecyclerView.getAdapter();
        adapter.unselect();
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
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);

                    mViewModel.setAddressPredictions(addresses);

                    GpsPredictionsAdapter gpsPredictionsAdapter = new GpsPredictionsAdapter(addresses, requireActivity());

                    mRecyclerView.setAdapter(gpsPredictionsAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireActivity(), getString(R.string.err_gps_disabled), Toast.LENGTH_SHORT).show();
            }
        });
    }
}