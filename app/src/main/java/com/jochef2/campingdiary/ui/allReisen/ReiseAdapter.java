package com.jochef2.campingdiary.ui.allReisen;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.relations.FullReise;

import java.util.Calendar;
import java.util.List;

public class ReiseAdapter extends RecyclerView.Adapter<ReiseAdapter.ViewHolder> {

    private List<FullReise> mDataset;
    private Context mContext;

    public ReiseAdapter(List<FullReise> dataset, Context c) {
        mDataset = dataset;
        mContext = c;
    }

    /**
     * sets data into each card and colors current
     *
     * @param holder   current card view
     * @param position position in mDataset
     */
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mName.setText(mDataset.get(position).mReise.getName());
        holder.mLand.setText(mDataset.get(position).mReise.getLand());

        if (position == mDataset.size() - 1 && mDataset.get(position).mReise.getBegin().before(Calendar.getInstance()) && mDataset.get(position).mReise.getEnd().after(Calendar.getInstance())) {

            holder.mCard.setCardBackgroundColor(R.attr.colorPrimaryVariant);
            holder.mCard.setOnClickListener(v -> Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(R.id.action_allReisenFragment_to_currentReiseFragment));
            holder.mDate.setText(mContext.getString(R.string.starting_time) + " " + mDataset.get(position).mReise.getBeginDate());
        } else {
            holder.mDate.setText(mDataset.get(position).mReise.getDates());
            // TODO: new Activity: ShowReise for old Reisen
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reise_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * @return count of items in recyclerview
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mDate;
        private TextView mLand;
        private CardView mCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tx_name);
            mDate = itemView.findViewById(R.id.tx_date);
            mLand = itemView.findViewById(R.id.tx_land);
            mCard = itemView.findViewById(R.id.card);
        }
    }
}

