package com.jochef2.campingdiary.ui.choosePlace.newPlace;

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
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;

public class GooglePredictionFragment extends Fragment {

    private static ChoosePlaceViewModel mViewModel;

    private static RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_prediction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);

        mRecyclerView = view.findViewById(R.id.recycler);

        mViewModel.getPlacePredictions().observe(getViewLifecycleOwner(), places -> {
            if (places != null) {
                GooglePredictionAdapter googlePredictionAdapter = new GooglePredictionAdapter(places, requireActivity());
                mRecyclerView.setAdapter(googlePredictionAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            } else {
                Toast.makeText(requireContext(), "Keine place predicitons", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void unselect() {
        GooglePredictionAdapter adapter = (GooglePredictionAdapter) mRecyclerView.getAdapter();
        adapter.unselect();
    }
}