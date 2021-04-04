package com.jochef2.campingdiary.ui.choosePlace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.choosePlace.newPlace.NewPlaceFragment;
import com.jochef2.campingdiary.ui.choosePlace.searchPlace.SearchPlaceFragment;
import com.jochef2.campingdiary.ui.newNight.NewNightViewModel;

import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ChoosePlaceFragment extends Fragment implements LifecycleObserver {

    private static BottomNavigationView mBottomNav;

    private ChoosePlaceViewModel mViewModel;
    private static ViewPager2 mViewPager;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private FloatingActionButton fabCheck;

    public static void setFirstPage() {
        mViewPager.setCurrentItem(0);
        mBottomNav.setSelectedItemId(R.id.mn_search);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_place_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                }
                Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
            }
        });

        // Check if GPS is available
        final LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            /*MaterialAlertDialogBuilder  alertDialogBuilder = new MaterialAlertDialogBuilder(requireActivity());
            alertDialogBuilder.
                    setTitle(getString(R.string.no_gps))
                    .setMessage(getString(R.string.err_gps_disabled))
                    .setCancelable(false)
                    .setPositiveButton(getString(android.R.string.yes), ((dialog, which) -> {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        requestLocatoinPermission();
                    }))
                    .setNegativeButton(getString(android.R.string.no), ((dialog, which) -> {
                        //TOD O: disable all selections except map
                        dialog.cancel();
                    }))
                    .show();*/
            Toast.makeText(requireActivity(), getString(R.string.err_gps_disabled), Toast.LENGTH_LONG).show();
        } else {
            requestLocationPermission();
        }
    }

    /**
     * requests location permission
     */
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            getLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.please_allow_gps), REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    /**
     * initializes cords of current location in ViewModel
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> mViewModel.setCurrentLocation(location));
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