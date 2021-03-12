package com.jochef2.campingdiary.ui.choosePlace.newPlace;

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
import com.jochef2.campingdiary.utils.Convert;

import java.util.List;

public class GpsPredictionsAdapter extends RecyclerView.Adapter<GpsPredictionsAdapter.ViewHolder> {

    private ChoosePlaceViewModel mViewModel;

    private List<String> mDataset;
    private FragmentActivity mFragmentActivity;

    private Listener mListener;
    private int lastSelected = -1;

    public GpsPredictionsAdapter(List<Address> dataset, FragmentActivity fragmentActivity) {
        mDataset = Convert.removeCountries(dataset);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txAddress.setText(mDataset.get(position));

        if (position == lastSelected) {
            holder.card.setCardBackgroundColor(R.attr.colorPrimaryVariant);
        } else {
            holder.card.setCardBackgroundColor(-15592942);
        }
    }

    public void setOnSelectListener(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onSelect(int position, MaterialCardView cardView);
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
                int copyLastSelected = lastSelected;
                lastSelected = getAdapterPosition();

                if (copyLastSelected == lastSelected) {
                    card.setCardBackgroundColor(-15592942);
                    lastSelected = -1;
                    notifyItemChanged(copyLastSelected);
                    mViewModel.setField(FIELDS.NULL);
                } else {
                    card.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                    notifyItemChanged(copyLastSelected);
                    notifyItemChanged(lastSelected);

                    mViewModel.setSelectedGpsPrediction(mDataset.get(lastSelected));
                    mViewModel.setField(FIELDS.GPS_PREDICTION);
                }
            });
        }
    }
}
