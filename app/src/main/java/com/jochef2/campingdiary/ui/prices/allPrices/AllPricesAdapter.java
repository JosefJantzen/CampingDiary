package com.jochef2.campingdiary.ui.prices.allPrices;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.relations.FullReise;

import java.util.Objects;

public class AllPricesAdapter extends RecyclerView.Adapter<AllPricesAdapter.ViewHolder>{
    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private final FragmentManager mFragmentManager;

    public AllPricesAdapter(LiveData<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public AllPricesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_price, parent, false);
        return new AllPricesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPricesAdapter.ViewHolder holder, int position) {
        //holder.mName
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mDataset.getValue()).mNights.size() + mDataset.getValue().mEvents.size() + mDataset.getValue().mFuels.size() + mDataset.getValue().mSADs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView mCard;
        private final TextView mName;
        private final TextView mPrice;
        private final ImageView mTypeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.card);
            mName = itemView.findViewById(R.id.tx_name);
            mPrice = itemView.findViewById(R.id.tx_price);
            mTypeIcon = itemView.findViewById(R.id.im_type);
        }
    }
}
