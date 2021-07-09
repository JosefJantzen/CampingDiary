package com.jochef2.campingdiary.ui.route.bottomSheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

public class RouteModalBottomSheet extends BottomSheetDialogFragment {

    private final int mRouteId;
    private final Context mContext;

    private LinearLayout open;
    private LinearLayout map;
    private LinearLayout edit;
    private LinearLayout delete;

    public RouteModalBottomSheet(int routeId, Context context) {
        mRouteId = routeId;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.route_modal_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit = view.findViewById(R.id.ll_edit);
        delete = view.findViewById(R.id.ll_delete);

        edit.setOnClickListener(v -> {
            //TODO: navigate to EditFuel
            this.dismiss();
        });

        delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(mContext)
                    .setTitle(getString(R.string.attention))
                    .setMessage(getString(R.string.dialog_delete_route))
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dismiss())
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        CurrentReiseViewModel.mReisenRepository.deleteRoute(mRouteId);
                        dialog.dismiss();
                    })
                    .show();
            this.dismiss();
        });
    }
}
