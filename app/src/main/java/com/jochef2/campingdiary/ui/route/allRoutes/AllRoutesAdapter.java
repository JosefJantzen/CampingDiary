package com.jochef2.campingdiary.ui.route.allRoutes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.relations.FullReise;
import com.jochef2.campingdiary.ui.route.bottomSheet.RouteModalBottomSheet;

import org.jetbrains.annotations.NotNull;

public class AllRoutesAdapter extends RecyclerView.Adapter<AllRoutesAdapter.ViewHolder>{

    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private final FragmentManager mFragmentManager;

    public AllRoutesAdapter(LiveData<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public AllRoutesAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_item, parent, false);
        return new AllRoutesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllRoutesAdapter.ViewHolder holder, int position) {
        holder.mTime.setText(mDataset.getValue().mRoutes.get(position).getTimeString());
        holder.mMileage.setText(String.valueOf(mDataset.getValue().mRoutes.get(position).getMileage()).replace(".0", "") + "km");
        holder.mCard.setOnLongClickListener(v -> {
            BottomSheetDialogFragment bottomSheet = new RouteModalBottomSheet( mDataset.getValue().mRoutes.get(position).getId(), mContext);
            bottomSheet.show(mFragmentManager, "FuelModalBottomSheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.getValue().mRoutes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView mCard;
        private final TextView mTime;
        private final TextView mMileage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.card);
            mTime = itemView.findViewById(R.id.tx_time);
            mMileage = itemView.findViewById(R.id.tx_mileage);
        }
    }
}
