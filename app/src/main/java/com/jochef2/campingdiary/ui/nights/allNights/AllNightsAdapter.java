package com.jochef2.campingdiary.ui.nights.allNights;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.relations.FullReise;
import com.jochef2.campingdiary.ui.nights.bottomSheet.NightModalBottomSheet;

import java.util.Objects;

public class AllNightsAdapter extends RecyclerView.Adapter<AllNightsAdapter.ViewHolder> {

    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private final FragmentManager mFragmentManager;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllNightsAdapter.ViewHolder holder, int position) {
        holder.mName.setText(Objects.requireNonNull(mDataset.getValue()).mNights.get(position).mNight.getName());
        holder.mDate.setText(mDataset.getValue().mNights.get(position).mNight.getDates());

        if (mDataset.getValue().getCurrentNightIds().contains(position)) {
            TypedValue value = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.colorPrimaryVariant, value, true);
            holder.mCard.setCardBackgroundColor(value.data);
        }

        holder.mCard.setOnClickListener(v -> {
            AllNightsFragmentDirections.ActionAllNightsFragmentToShowNightFragment action = AllNightsFragmentDirections.actionAllNightsFragmentToShowNightFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setNightId(mDataset.getValue().mNights.get(position).mNight.getId());
            Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(action);
        });

        holder.mCard.setOnLongClickListener(v -> {
            AllNightsFragmentDirections.ActionAllNightsFragmentToShowNightFragment action = AllNightsFragmentDirections.actionAllNightsFragmentToShowNightFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setNightId(mDataset.getValue().mNights.get(position).mNight.getId());

            BottomSheetDialogFragment bottomSheet = new NightModalBottomSheet(Objects.requireNonNull(mDataset.getValue()).mNights.get(position).mPlace, action, mContext);
            bottomSheet.show(mFragmentManager, "NightModalBottomSheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mDataset.getValue()).mNights.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView mCard;
        private final TextView mName;
        private final TextView mDate;
        private final ImageView mIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.card);
            mName = itemView.findViewById(R.id.tx_name);
            mDate = itemView.findViewById(R.id.tx_date);
            mIcon = itemView.findViewById(R.id.im_cat);
        }
    }
}
