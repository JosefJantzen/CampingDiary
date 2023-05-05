package com.josefjantzen.campingdiary.ui.maps.showPlace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.entities.Place;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapShowPlaceDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapShowPlaceDialogFragment extends DialogFragment implements OnMapReadyCallback {

    private static String mTitle;
    private static String mSnippet;
    private static LatLng mLatLng;

    private MapView mMapView;
    private GoogleMap mMap;
    private FloatingActionButton mFabClose;

    public static MapShowPlaceDialogFragment newInstance(FragmentManager fragmentManager, String title, String snippet, LatLng latLng) {
        mTitle = title;
        mSnippet = snippet;
        mLatLng = latLng;

        MapShowPlaceDialogFragment fragment = new MapShowPlaceDialogFragment();
        fragment.show(fragmentManager, "MapShowPlaceDialogFragment");
        return fragment;
    }

    public static MapShowPlaceDialogFragment newInstance(FragmentManager fragmentManager, Place place) {
        mTitle = place.getPlaceName();
        if (place.getAddressString() != null) {
            mSnippet = place.getAddressString();
        }
        else if (place.getAddressObject().getAddressLine(0) != null){
            mSnippet = place.getAddressObject().getAddressLine(0);
        }
        else if (place.getCords() != null){
            mSnippet = place.getCordsString();
        }
        mLatLng = place.getCords().toLatLng();

        MapShowPlaceDialogFragment fragment = new MapShowPlaceDialogFragment();
        fragment.show(fragmentManager, "MapShowPlaceDialogFragment");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_show_place_dialog, container, false);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mFabClose = view.findViewById(R.id.fab_close);
        mFabClose.setOnClickListener(v -> dismiss());
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings settings = googleMap.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

        if (mLatLng != null) {
            if (mTitle == null) {
                mTitle = getString(R.string.unnammed);
            }
            mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(mTitle)
                .snippet(mSnippet)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));
        }
    }
}