package com.jochef2.campingdiary.ui.events.newEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Event;
import com.jochef2.campingdiary.data.values.EventCategory;

import org.jetbrains.annotations.NotNull;

public class NewEventCategorySelectorFragment extends Fragment {

    FlexMaterialButtonToogleGroup mToogleGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event_category_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToogleGroup = view.findViewById(R.id.toggle_group);

        mToogleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            MaterialButton btn = view.findViewById(checkedId);
            EventCategory cat = EventCategory.valueOf(btn.getTag().toString());

            if (cat == EventCategory.NIGHT) {
                //TODO: navigate to newNight
            }
            else if (cat == EventCategory.FUEL) {
                //TODO: navigate to newFuel
            }
            else if (cat == EventCategory.SAD) {
                //TODO: navigate to newSAD
            }
            else {
                Event event = NewEventFragment.mViewModel.mEvent.getValue();
                event.setCat(cat);
                NewEventFragment.mViewModel.mEvent.setValue(event);
                NewEventFragment.setTab(1);
            }
        });
    }
}