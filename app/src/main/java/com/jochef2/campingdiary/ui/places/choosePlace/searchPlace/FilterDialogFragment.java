package com.jochef2.campingdiary.ui.places.choosePlace.searchPlace;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.RangeSlider;
import com.jochef2.campingdiary.R;

import java.util.List;

public class FilterDialogFragment extends DialogFragment {

    private final List<Float> mRangeDistanceValues;
    private PlaceFilterListener mPlaceFilterListener;
    private RangeSlider mSliderDistance;

    public FilterDialogFragment(List<Float> rangeDistanceValues) {
        mRangeDistanceValues = rangeDistanceValues;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.search_place_filter_dialog, null);

        mSliderDistance = view.findViewById(R.id.range_distance);

        if (mRangeDistanceValues != null) {
            mSliderDistance.setValueFrom(mRangeDistanceValues.get(0));
            mSliderDistance.setValueTo(mRangeDistanceValues.get(1));
            mSliderDistance.setValues(mRangeDistanceValues);
            mSliderDistance.addOnChangeListener((slider, value, fromUser) -> {

            });
        }

        builder.setView(view)
                .setTitle(R.string.filter)
                .setPositiveButton(R.string.apply, (dialog, which) -> {
                    mPlaceFilterListener.onResult(mSliderDistance.getValues().get(0), mSliderDistance.getValues().get(1));
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    this.getDialog().cancel();
                    mPlaceFilterListener.onCancel();
                });
        return builder.create();
    }

    public void setListener(PlaceFilterListener placeFilterListener) {
        mPlaceFilterListener = placeFilterListener;
    }

    public interface PlaceFilterListener {
        void onResult(float distanceMin, float distanceMax);

        void onCancel();
    }
}
