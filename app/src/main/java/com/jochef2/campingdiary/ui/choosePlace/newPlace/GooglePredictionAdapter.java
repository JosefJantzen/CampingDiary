package com.jochef2.campingdiary.ui.choosePlace.newPlace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jochef2.campingdiary.R;

import java.util.List;

public class GooglePredictionAdapter extends RecyclerView.Adapter<GooglePredictionAdapter.ViewHolder> {

    private List<PlaceLikelihood> mDataset;

    public GooglePredictionAdapter(List<PlaceLikelihood> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.google_prediction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txName.setText(mDataset.get(position).getPlace().getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txName = itemView.findViewById(R.id.tx_name);
        }
    }
}
