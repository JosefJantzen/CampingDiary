package com.jochef2.campingdiary.ui.allNights;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.relations.FullReise;
import com.jochef2.campingdiary.utils.Sort;

import java.util.Objects;

public class AllNightsAdapter extends RecyclerView.Adapter<AllNightsAdapter.ViewHolder> {

    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private FragmentManager mFragmentManager;

    public AllNightsAdapter(LiveData<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.night_item, parent, false);
        return new AllNightsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllNightsAdapter.ViewHolder holder, int position) {
        holder.mName.setText(Objects.requireNonNull(mDataset.getValue()).mNights.get(position).mNight.getName());
        holder.mDate.setText(mDataset.getValue().mNights.get(position).mNight.getDates());

        if (Sort.getCurrentNightIds(mDataset.getValue().mNights).contains(position)) {
            TypedValue value = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.colorPrimaryVariant, value, true);
            holder.mCard.setCardBackgroundColor(value.data);
        }

        holder.mCard.setOnClickListener(v -> {
            AllNightsFragmentDirections.ActionAllNightsFragmentToShowNightFragment action = AllNightsFragmentDirections.actionAllNightsFragmentToShowNightFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setNightPosition(position);
            Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(action);
        });

        holder.mCard.setOnLongClickListener(v -> {
            BottomSheetDialogFragment bottomSheet = new ModalBottomSheet(position);
            bottomSheet.show(mFragmentManager, "ModalBottomSheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mDataset.getValue()).mNights.size();
    }

    public static class ModalBottomSheet extends BottomSheetDialogFragment {

        private int mPosition;

        private LinearLayout open;
        private LinearLayout map;
        private LinearLayout edit;
        private LinearLayout delete;

        public ModalBottomSheet(int position) {
            mPosition = position;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.all_nights_modal_bottom_sheet, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            open = view.findViewById(R.id.ll_open);
            map = view.findViewById(R.id.ll_map);
            edit = view.findViewById(R.id.ll_edit);
            delete = view.findViewById(R.id.ll_delete);

            open.setOnClickListener(v -> {
                AllNightsFragmentDirections.ActionAllNightsFragmentToShowNightFragment action = AllNightsFragmentDirections.actionAllNightsFragmentToShowNightFragment();
                action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
                action.setNightPosition(mPosition);
                Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(action);
                this.dismiss();
            });

            map.setOnClickListener(v -> {
                //TODO: navigate to fullscreen Map with Marker
            });

            edit.setOnClickListener(v -> {
                //TODO: navigate to EditNight
            });

            delete.setOnClickListener(v -> {
                //TODO: delete Night after Alert
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView mCard;
        private TextView mName;
        private TextView mDate;
        private ImageView mIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.card);
            mName = itemView.findViewById(R.id.tx_name);
            mDate = itemView.findViewById(R.id.tx_date);
            mIcon = itemView.findViewById(R.id.ic_cat);
        }
    }
}
