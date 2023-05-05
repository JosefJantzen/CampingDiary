package com.josefjantzen.campingdiary.ui.reisen.allReisen;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.relations.FullReise;
import com.josefjantzen.campingdiary.ui.reisen.bottomSheet.ReiseModalBottomSheet;

import java.util.Calendar;
import java.util.List;

public class ReiseAdapter extends RecyclerView.Adapter<ReiseAdapter.ViewHolder> {

    private final List<FullReise> mDataset;
    private final Context mContext;
    private final FragmentManager mFragmentManager;

    public ReiseAdapter(List<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
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
            holder.mCard.setOnLongClickListener(v -> {
                BottomSheetDialogFragment bottomSheet = new ReiseModalBottomSheet(mDataset.get(position).mReise.getId(), AllReisenFragmentDirections.actionAllReisenFragmentToCurrentReiseFragment(), mContext);
                bottomSheet.show(mFragmentManager, "ReiseModalBottomSheet");
                return true;
            });
        } else {
            holder.mDate.setText(mDataset.get(position).mReise.getDates());
            holder.mCard.setOnClickListener(view -> {

            });
            holder.mCard.setOnLongClickListener(v -> {
                BottomSheetDialogFragment bottomSheet = new ReiseModalBottomSheet(mDataset.get(position).mReise.getId(), null, mContext);
                bottomSheet.show(mFragmentManager, "ReiseModalBottomSheet");
                return true;
            });
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
        private final TextView mName;
        private final TextView mDate;
        private final TextView mLand;
        private final CardView mCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tx_name);
            mDate = itemView.findViewById(R.id.tx_date);
            mLand = itemView.findViewById(R.id.tx_countries);
            mCard = itemView.findViewById(R.id.card);
        }
    }
}

