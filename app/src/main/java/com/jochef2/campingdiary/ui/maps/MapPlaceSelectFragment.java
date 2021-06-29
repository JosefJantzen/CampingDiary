package com.jochef2.campingdiary.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.material.button.MaterialButton;
import com.jochef2.campingdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MapPlaceSelectFragment extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnPoiClickListener {

    private static LatLng mLatLng;
    private static String mTitle;
    MaterialButton btnCancel;
    MaterialButton btnSelect;
    private GoogleMap mMap;
    private MapView mMapView;
    private PointOfInterest mPOI;
    private MapPlaceSelectInterface mInterface;

    /**
     * Creates new Instance of DialogFragment
     *
     * @param fragmentManager FragmentManager of the child
     * @param title           current title of the place
     * @param latLng          Cords of current selected place on map of current location
     * @return The new instance of the DialogFragment
     */
    public static MapPlaceSelectFragment showDialog(FragmentManager fragmentManager, String title, LatLng latLng) {
        mLatLng = latLng;
        mTitle = title;

        MapPlaceSelectFragment newFragment = new MapPlaceSelectFragment();
        newFragment.show(fragmentManager, "MapPlaceSelectDialogFragment");
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_place_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            Objects.requireNonNull(getDialog()).cancel();
        });

        btnSelect = view.findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(v -> {
            Objects.requireNonNull(getDialog()).dismiss();
            if (mPOI == null) {
                mInterface.onResult(new PointOfInterest(mLatLng, null, null));
            } else {
                mInterface.onResult(mPOI);
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * Makes Dialog big
     */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    /**
     * @param anInterface Interface for Callback of the result
     */
    public void setInterface(MapPlaceSelectInterface anInterface) {
        mInterface = anInterface;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationClickListener(this);
        }
    }

    /**
     * Called when Mao is ready and adjusts settings
     *
     * @param googleMap The map
     */
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings settings = googleMap.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationClickListener(this);
        }

        if (mLatLng != null) {

            mMap.addMarker(new MarkerOptions()
                    .position(mLatLng)
                    .title(mTitle)
                    .snippet(mLatLng.latitude + ", " + mLatLng.longitude)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));
        }

        // Adds marker when clicked somewhere at the map
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(mTitle)
                    .snippet(latLng.latitude + ", " + mLatLng.longitude)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mLatLng = latLng;
            mPOI = null;
        });

        mMap.setOnPoiClickListener(this);
    }

    /**
     * Adds marker when click on my location
     *
     * @param location
     */
    @Override
    public void onMyLocationClick(@NonNull @NotNull Location location) {
        mMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(mTitle)
                .snippet(latLng.latitude + ", " + latLng.longitude)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mLatLng = latLng;
        mPOI = null;
    }

    /**
     * Adds marker when click on a POI
     *
     * @param poi POI that's clicked
     */
    @Override
    public void onPoiClick(@NonNull @NotNull PointOfInterest poi) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(poi.latLng)
                .title(poi.name)
                .snippet(poi.latLng.latitude + ", " + poi.latLng.longitude)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng, 16));
        mPOI = poi;
    }
}