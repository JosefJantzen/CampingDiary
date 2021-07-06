package com.jochef2.campingdiary.ui.events.bottomSheet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.ui.maps.showPlace.MapShowPlaceDialogFragment;

public class EventModalBottomSheet extends BottomSheetDialogFragment {

    private final Place mPlace;
    private final NavDirections mAction;
    private final Context mContext;

    private LinearLayout open;
    private LinearLayout map;
    private LinearLayout edit;
    private LinearLayout delete;

    public EventModalBottomSheet(Place place, NavDirections action, Context context) {
        mPlace = place;
        mAction = action;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_modal_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        open = view.findViewById(R.id.ll_open);
        map = view.findViewById(R.id.ll_map);
        edit = view.findViewById(R.id.ll_edit);
        delete = view.findViewById(R.id.ll_delete);

        open.setOnClickListener(v -> {
            Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(mAction);
            this.dismiss();
        });

        map.setOnClickListener(v -> {
            if (mPlace != null) {
                MapShowPlaceDialogFragment.newInstance(getParentFragmentManager(), mPlace);
            } else {
                Toast.makeText(mContext, getString(R.string.toast_no_place), Toast.LENGTH_SHORT).show();
            }
            this.dismiss();
        });

        edit.setOnClickListener(v -> {
            //TODO: navigate to EditEvent
            this.dismiss();
        });

        delete.setOnClickListener(v -> {
            //TODO: delete Event after Alert
            this.dismiss();
        });
    }
}
