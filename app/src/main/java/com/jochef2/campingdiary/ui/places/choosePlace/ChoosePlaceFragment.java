package com.jochef2.campingdiary.ui.places.choosePlace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.events.newEvent.NewEventViewModel;
import com.jochef2.campingdiary.ui.fuel.newFuel.NewFuelViewModel;
import com.jochef2.campingdiary.ui.nights.newNight.NewNightViewModel;
import com.jochef2.campingdiary.ui.places.choosePlace.newPlace.NewPlaceFragment;
import com.jochef2.campingdiary.ui.places.choosePlace.searchPlace.SearchPlaceFragment;

import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ChoosePlaceFragment extends Fragment implements LifecycleObserver {

    private static BottomNavigationView mBottomNav;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static ViewPager2 mViewPager;
    private static ChoosePlaceViewModel mViewModel;
    private static LocationCallback mLocationCallback;
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LocationRequest mLocationRequest;

    private FloatingActionButton fabCheck;

    private static Context sContext;
    private static ChoosePlaceFragment sThis;
    private static FragmentActivity sFragmentActivity;

    /**
     * sets ViewPager to first Page: SearchFragment
     */
    public static void setFirstPage() {
        mViewPager.setCurrentItem(0);
        mBottomNav.setSelectedItemId(R.id.mn_search);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_place_fragment, container, false);
    }

    /**
     * requests location permission
     */
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public static void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        if (EasyPermissions.hasPermissions(sContext, perms)) {
            getLocation();
        } else {
            EasyPermissions.requestPermissions(sThis, sContext.getString(R.string.please_allow_gps), REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    /**
     * initializes cords of current location in ViewModel
     */
    @SuppressLint("MissingPermission")
    private static void getLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(sFragmentActivity, location -> {
            if (location != null) {
                mViewModel.setCurrentLocation(location);
            } else {
                Log.e("TAG", "getLastLocation = null");
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "MissingPermission"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sContext = requireContext();
        sThis = this;
        sFragmentActivity = requireActivity();

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);
        mViewModel.setEvent(ChoosePlaceFragmentArgs.fromBundle(requireArguments()).getCat());

        mBottomNav = view.findViewById(R.id.bottom_nav_menu);
        mViewPager = view.findViewById(R.id.viewpager);
        fabCheck = view.findViewById(R.id.fab_check);

        ScreenPagerAdapter screenPagerAdapter = new ScreenPagerAdapter(requireActivity());
        mViewPager.setAdapter(screenPagerAdapter);
        mViewPager.setUserInputEnabled(false);

        // sets selected tab at startup
        //mViewPager.setCurrentItem(1, false);
        //mBottomNav.setSelectedItemId(R.id.mn_new_place);

        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mn_search:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.mn_new_place:
                    mViewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });

        // saves selected place and gives place id to the ViewModel of requested Fragment
        fabCheck.setOnClickListener(v -> {
            int id = mViewModel.save();
            if (id != -1) {
                switch (mViewModel.getEvent()) {
                    case NIGHT:
                        new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewNightViewModel.class).setPlace(id);
                        break;
                    case EVENT:
                        new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewEventViewModel.class).setPlace(id);
                        break;
                    case FUEL:
                        new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewFuelViewModel.class).setPlace(id);
                        break;
                }
                Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // gets current location if last know location is null
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    Log.e("TAG", "Location Callback Request Result is null");
                    return;
                }
                requestLocationPermission();
            }
        };

        // Checks if GPS is available
        final LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(requireActivity());
            alertDialogBuilder.
                    setTitle(getString(R.string.no_gps))
                    .setMessage(getString(R.string.err_gps_disabled))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), ((dialog, which) -> {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        requestLocationPermission();
                    }))
                    .setNegativeButton(getString(R.string.no), ((dialog, which) -> {
                        //TODO: disable all selections except map
                        dialog.cancel();
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
                    }))
                    .show();
        } else {
            requestLocationPermission();
        }
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

    /**
     * Same as onActivityCreated
     * Set's title to R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.choose_place);
    }

    /**
     * for onActivityCreated
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getLifecycle().addObserver(this);
    }

    /**
     * for onActivityCreated
     */
    @Override
    public void onDetach() {
        super.onDetach();
        getLifecycle().removeObserver(this);
    }

    private static class ScreenPagerAdapter extends FragmentStateAdapter {

        public ScreenPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SearchPlaceFragment();
                case 1:
                    return new NewPlaceFragment();
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