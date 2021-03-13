package com.jochef2.campingdiary.ui.choosePlace.newPlace;

import android.annotation.SuppressLint;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel.FIELDS;

import java.util.List;

public class GpsPredictionsAdapter extends RecyclerView.Adapter<GpsPredictionsAdapter.ViewHolder> {

    private ChoosePlaceViewModel mViewModel;

    private List<Address> mDataset;
    private FragmentActivity mFragmentActivity;

    //private int lastSelected = -1;

    public GpsPredictionsAdapter(List<Address> dataset, FragmentActivity fragmentActivity) {
        mDataset = dataset;
        mFragmentActivity = fragmentActivity;
        mViewModel = new ViewModelProvider(mFragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(mFragmentActivity.getApplication())).get(ChoosePlaceViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gps_prediction_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String address = mDataset.get(position).getAddressLine(0);
        String[] parts = address.split(",");
        holder.txAddress.setText(parts[0] + "," + parts[1]);

        if (position == mViewModel.getSelectedGpsPrediction()) {
            holder.card.setCardBackgroundColor(R.attr.colorPrimaryVariant);
        } else {
            holder.card.setCardBackgroundColor(-15592942);
        }
    }

    public void unselect() {
        int copy = mViewModel.getSelectedGpsPrediction();
        mViewModel.setSelectedGpsPrediction(-1);
        notifyItemChanged(copy);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txAddress;
        private MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txAddress = itemView.findViewById(R.id.tx_address);
            card = itemView.findViewById(R.id.card);

            card.setOnClickListener(v -> {
                int copyLastSelected = mViewModel.getSelectedGpsPrediction();
                mViewModel.setSelectedGpsPrediction(getAdapterPosition());

                if (copyLastSelected == mViewModel.getSelectedGpsPrediction()) {
                    card.setCardBackgroundColor(-15592942);
                    mViewModel.setSelectedGpsPrediction(-1);
                    notifyItemChanged(copyLastSelected);
                    //mViewModel.setSelectedGpsPrediction(lastSelected);
                    mViewModel.setField(FIELDS.NULL);
                } else {
                    card.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                    notifyItemChanged(copyLastSelected);
                    notifyItemChanged(mViewModel.getSelectedGpsPrediction());

                    //mViewModel.setSelectedGpsPrediction(lastSelected);
                    mViewModel.setField(FIELDS.GPS_PREDICTION);
                }
            });
        }
    }
}
