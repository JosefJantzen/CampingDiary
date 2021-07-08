package com.jochef2.campingdiary.ui.sad.allSads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.jochef2.campingdiary.data.values.SADCategory;
import com.jochef2.campingdiary.ui.sad.bottomSheet.SADModalBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AllSADsAdapter extends RecyclerView.Adapter<AllSADsAdapter.ViewHolder>{

    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private final FragmentManager mFragmentManager;

    public AllSADsAdapter(LiveData<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public AllSADsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sad_item, parent, false);
        return new AllSADsAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull @NotNull AllSADsAdapter.ViewHolder holder, int position) {
        holder.mName.setText(mDataset.getValue().mSADs.get(position).mSAD.getName());
        holder.mDate.setText(mDataset.getValue().mSADs.get(position).mSAD.getTimeString());
        for (SADCategory cat : mDataset.getValue().mSADs.get(position).mSAD.getCats()) {
            switch (cat) {
                case WATER:
                    holder.mWater.setColorFilter(R.attr.colorSecondary);
                    holder.mWaterText.setText(mDataset.getValue().mSADs.get(position).mSAD.getWaterString());
                    break;
                case GREY_WATER:
                    holder.mGreyWater.setColorFilter(R.attr.colorSecondary);
                    break;
                case TOILETTE:
                    holder.mToilette.setColorFilter(R.attr.colorSecondary);
                    break;
                case GARBAGE:
                    holder.mGarbage.setColorFilter(R.attr.colorSecondary);
                    break;
                case GAS:
                    holder.mGas.setColorFilter(R.attr.colorSecondary);
                    break;
            }
        }
        holder.mCard.setOnClickListener(v -> {
            AllSADsFragmentDirections.ActionAllSADsFragmentToShowSADFragment action = AllSADsFragmentDirections.actionAllSADsFragmentToShowSADFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setSadId(mDataset.getValue().mSADs.get(position).mSAD.mId);
            Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(action);
        });

        holder.mCard.setOnLongClickListener(v -> {
            AllSADsFragmentDirections.ActionAllSADsFragmentToShowSADFragment action = AllSADsFragmentDirections.actionAllSADsFragmentToShowSADFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setSadId(mDataset.getValue().mSADs.get(position).mSAD.mId);

            BottomSheetDialogFragment bottomSheet = new SADModalBottomSheet(Objects.requireNonNull(mDataset.getValue()).mSADs.get(position).mPlace, mDataset.getValue().mSADs.get(position).mSAD.mId, action, mContext);
            bottomSheet.show(mFragmentManager, "SADModalBottomSheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.getValue().mSADs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView mCard;
        private final TextView mDate;
        private final TextView mName;
        private final TextView mWaterText;
        private final ImageView mWater;
        private final ImageView mGreyWater;
        private final ImageView mToilette;
        private final ImageView mGarbage;
        private final ImageView mGas;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mCard = itemView.findViewById(R.id.card);
            mDate = itemView.findViewById(R.id.tx_date);
            mName = itemView.findViewById(R.id.tx_name);
            mWaterText = itemView.findViewById(R.id.tx_water);
            mWater = itemView.findViewById(R.id.im_water);
            mGreyWater = itemView.findViewById(R.id.im_grey_water);
            mToilette = itemView.findViewById(R.id.im_toilette);
            mGarbage = itemView.findViewById(R.id.im_garbage);
            mGas = itemView.findViewById(R.id.im_gas);
        }
    }
}
