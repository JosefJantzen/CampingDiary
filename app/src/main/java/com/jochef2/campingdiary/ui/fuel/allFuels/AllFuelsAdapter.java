package com.jochef2.campingdiary.ui.fuel.allFuels;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jochef2.campingdiary.ui.fuel.bottomSheet.FuelModalBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AllFuelsAdapter extends RecyclerView.Adapter<AllFuelsAdapter.ViewHolder>{

    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private final FragmentManager mFragmentManager;

    public AllFuelsAdapter(LiveData<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public AllFuelsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fuel_item, parent, false);
        return new AllFuelsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllFuelsAdapter.ViewHolder holder, int position) {
        holder.mName.setText(mDataset.getValue().mFuels.get(position).mFuel.getName());
        holder.mDate.setText(mDataset.getValue().mFuels.get(position).mFuel.getTimeString());
        holder.mPrice.setText(mDataset.getValue().mFuels.get(position).mFuel.getPrice().toString().replace(".", ","));
        holder.mCard.setOnClickListener(v -> {
            AllFuelsFragmentDirections.ActionAllFuelsFragmentToShowFuelFragment action = AllFuelsFragmentDirections.actionAllFuelsFragmentToShowFuelFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setFuelId(mDataset.getValue().mFuels.get(position).mFuel.getId());
            Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(action);
        });

        holder.mCard.setOnLongClickListener(v -> {
            AllFuelsFragmentDirections.ActionAllFuelsFragmentToShowFuelFragment action = AllFuelsFragmentDirections.actionAllFuelsFragmentToShowFuelFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setFuelId(mDataset.getValue().mFuels.get(position).mFuel.getId());

            BottomSheetDialogFragment bottomSheet = new FuelModalBottomSheet(Objects.requireNonNull(mDataset.getValue()).mFuels.get(position).mPlace, mDataset.getValue().mFuels.get(position).mFuel.getId(), action, mContext);
            bottomSheet.show(mFragmentManager, "FuelModalBottomSheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.getValue().mFuels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView mCard;
        private final TextView mDate;
        private final TextView mName;
        private final TextView mFuel;
        private final TextView mPrice;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mCard = itemView.findViewById(R.id.card);
            mDate = itemView.findViewById(R.id.tx_date);
            mName = itemView.findViewById(R.id.tx_name);
            mFuel = itemView.findViewById(R.id.tx_fuel);
            mPrice = itemView.findViewById(R.id.tx_price);
        }
    }
}
