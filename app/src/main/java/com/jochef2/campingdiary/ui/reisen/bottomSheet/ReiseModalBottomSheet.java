package com.jochef2.campingdiary.ui.reisen.bottomSheet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

public class ReiseModalBottomSheet extends BottomSheetDialogFragment {

    private final int mReiseId;
    private final NavDirections mAction;
    private final Context mContext;

    private LinearLayout open;
    private LinearLayout edit;
    private LinearLayout delete;

    public ReiseModalBottomSheet(int reiseId, NavDirections action, Context context) {
        mReiseId = reiseId;
        mAction = action;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reise_modal_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        open = view.findViewById(R.id.ll_open);
        edit = view.findViewById(R.id.ll_edit);
        delete = view.findViewById(R.id.ll_delete);

        open.setOnClickListener(v -> {
            if (mAction != null) {
                Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(mAction);
            }
            this.dismiss();
        });

        edit.setOnClickListener(v -> {
            //TODO: navigate to EditEvent
            this.dismiss();
        });

        delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(mContext)
                    .setTitle(getString(R.string.attention))
                    .setMessage(getString(R.string.dialog_delete_reise))
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dismiss())
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        CurrentReiseViewModel.mReisenRepository.deleteReise(mReiseId);
                        dialog.dismiss();
                    })
                    .show();
            this.dismiss();
        });
    }
}
