package com.josefjantzen.campingdiary.ui.places.choosePlace.newPlace;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GooglePredictionFragment extends Fragment {

    private static ChoosePlaceViewModel mViewModel;

    public static List<Place.Field> mPlaceFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private PlacesClient placesClient;

    private static RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Animator mAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_prediction, container, false);
    }

    /**
     * unselects selected card in recycler
     */
    public static void unselect() {
        GooglePredictionAdapter adapter = (GooglePredictionAdapter) mRecyclerView.getAdapter();
        adapter.unselect();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);

        mRecyclerView = view.findViewById(R.id.recycler);
        mProgressBar = view.findViewById(R.id.progress_google_predictions);

        requestLocationPermission();

        mViewModel.getPlacePredictions().observe(getViewLifecycleOwner(), places -> {
            if (places != null) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);

                GooglePredictionAdapter googlePredictionAdapter = new GooglePredictionAdapter(places, requireActivity());
                mRecyclerView.setAdapter(googlePredictionAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

            } else {
                Log.e("TAG", "Keine place predicitons");
                if (mProgressBar.getVisibility() != View.VISIBLE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * requests location permission
     */
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            getCurrentPlace();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.please_allow_gps), REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    /**
     * initializes List of Places from Places Api prediction in ViewModel
     */
    private void getCurrentPlace() {
        if(!Places.isInitialized()) {
            //TODO: API-KEY
            Places.initialize(requireActivity().getApplicationContext(), getString(R.string.MAPS_API_KEY));
        }
        placesClient = Places.createClient(requireActivity());

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(mPlaceFields);

        @SuppressLint("MissingPermission") Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                mViewModel.setPlacePredictions(response.getPlaceLikelihoods());
            } else {
                Exception exception = task.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
                        if (location != null) {
                            requestLocationPermission();
                            mProgressBar.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    /**
     * called if permission result is selected
     *
     * @param requestCode  code to identify request
     * @param permissions  permissions given
     * @param grantResults result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}