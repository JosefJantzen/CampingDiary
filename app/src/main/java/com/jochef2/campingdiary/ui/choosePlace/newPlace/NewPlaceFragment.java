package com.jochef2.campingdiary.ui.choosePlace.newPlace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NewPlaceFragment extends Fragment {

    private ChoosePlaceViewModel mViewModel;

    public static List<Place.Field> mPlaceFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlacesClient placesClient;

    private ViewPager2 mPredictionsPager;
    private AutocompleteSupportFragment mAutocompleteFragment;
    private TabLayout mTabLayout;
    private TextView txLatitude;
    private TextView txLongitude;
    private TextInputEditText etName;
    private MaterialButton btnAutocomplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_place, container, false);
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);

        mPredictionsPager = view.findViewById(R.id.pager_predictions);
        mTabLayout = view.findViewById(R.id.tabs_prediction);
        txLatitude = view.findViewById(R.id.tx_latitude);
        txLongitude = view.findViewById(R.id.tx_longitude);
        etName = view.findViewById(R.id.et_name);
        btnAutocomplete = view.findViewById(R.id.btn_autocomplete);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("NAME"));
        }

        mPredictionsPager.setAdapter(new PredictionsPagerAdapter(requireActivity()));
        new TabLayoutMediator(mTabLayout, mPredictionsPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_gps);
                    tab.setText(R.string.gps);
                    break;
                case 1:
                    tab.setIcon(R.drawable.googleg_disabled_color_18);
                    tab.setText(R.string.google);
                    break;
            }

        }).attach();

        //TODO: API-KEY
        Places.initialize(requireActivity().getApplicationContext(), "AIzaSyBzZ_DJaH2cu3WN-30UY6BcabQIoT3bnG0");
        placesClient = Places.createClient(requireActivity());

        btnAutocomplete.setOnClickListener(v -> {
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, mPlaceFields)
                    .build(requireContext());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        requestLocatoinPermission();

        mViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            txLatitude.setText(Double.toString(location.getLatitude()));
            txLongitude.setText(Double.toString(location.getLongitude()));
        });

        /*mAutocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        mAutocompleteFragment.setPlaceFields(mPlaceFields);
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d("TAG", "Place: " + place.getName());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("TAG", "Error: " + status);
            }
        });*/
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocatoinPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            getLocation();
            getCurrentPlace();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.please_allow_gps), REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> mViewModel.setCurrentLocation(location));
    }

    private void getCurrentPlace() {
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
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NAME", Objects.requireNonNull(etName.getText()).toString());
    }

    /**
     * Adpater for viewPager of Perdictions TabLayout
     */
    private static class PredictionsPagerAdapter extends FragmentStateAdapter {
        public PredictionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new GpsPredictionFragment();
                case 1:
                    return new GooglePredictionFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}