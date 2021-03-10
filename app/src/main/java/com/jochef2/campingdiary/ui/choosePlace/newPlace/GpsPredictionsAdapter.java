package com.jochef2.campingdiary.ui.choosePlace.newPlace;

import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.utils.Convert;

import java.util.List;

public class GpsPredictionsAdapter extends RecyclerView.Adapter<GpsPredictionsAdapter.ViewHolder> {

    private final List<String> mDataset;

    public GpsPredictionsAdapter(List<Address> dataset) {
        mDataset = Convert.removeCountries(dataset);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gps_prediction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txAddress.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txAddress = itemView.findViewById(R.id.tx_address);
        }
    }
}
