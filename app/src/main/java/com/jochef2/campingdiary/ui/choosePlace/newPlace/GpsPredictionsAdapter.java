package com.jochef2.campingdiary.ui.choosePlace.newPlace;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.location.Address;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel.FIELDS;

import java.util.List;

public class GpsPredictionsAdapter extends RecyclerView.Adapter<GpsPredictionsAdapter.ViewHolder> {

    private final ChoosePlaceViewModel mViewModel;

    private final List<Address> mDataset;
    private final FragmentActivity mFragmentActivity;

    MaterialCardView cardView;
    int defaultColor;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public GpsPredictionsAdapter(List<Address> dataset, FragmentActivity fragmentActivity) {
        mDataset = dataset;
        mFragmentActivity = fragmentActivity;
        mViewModel = new ViewModelProvider(mFragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(mFragmentActivity.getApplication())).get(ChoosePlaceViewModel.class);

        cardView = new MaterialCardView(new ContextThemeWrapper(mFragmentActivity, R.style.Widget_MaterialComponents_CardView));
        defaultColor = R.attr.colorSurface;
        //Log.d("TAG", defaultColor.getDefaultColor() + "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gps_prediction_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor", "ResourceType"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String address = mDataset.get(position).getAddressLine(0);
        String address = "N/A";

        if (mDataset.get(0).getFeatureName() != null) {
            address = mDataset.get(0).getFeatureName();
        } else if (mDataset.get(0).getThoroughfare() != null) {
            address = mDataset.get(0).getThoroughfare();
            if (mDataset.get(0).getSubThoroughfare() != null) {
                address += " " + mDataset.get(0).getSubThoroughfare();
            }

            if (mDataset.get(0).getLocality() != null) {
                address += ", " + mDataset.get(0).getLocality();
            }
        } else if (mDataset.get(0).getLocality() != null) {
            address = mDataset.get(0).getLocality();
        } else if (mDataset.get(0).hasLatitude() && mDataset.get(0).hasLongitude()) {
            address = mDataset.get(0).getLatitude() + ", " + mDataset.get(0).getLongitude() + " (lat, lng)";
        }


        holder.txAddress.setText(address);

        if (position == mViewModel.getSelectedGpsPrediction()) {
            holder.card.setCardBackgroundColor(2130903247);
            holder.card.setActivated(true);
        } else {
            switch (mFragmentActivity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    holder.card.setCardBackgroundColor(-15592942);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    holder.card.setCardBackgroundColor(-1);
                    break;
            }
            holder.card.setActivated(false);
        }
    }

    public void unselect() {
        int copy = mViewModel.getSelectedGpsPrediction();
        if (mViewModel.mField.getValue() != FIELDS.GPS_PREDICTION) {
            mViewModel.setSelectedGpsPrediction(-1);
        }
        notifyItemChanged(copy);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txAddress;
        private final MaterialCardView card;

        @SuppressLint({"ResourceAsColor", "ResourceType"})
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txAddress = itemView.findViewById(R.id.tx_address);
            card = itemView.findViewById(R.id.card);

            card.setOnClickListener(v -> {
                int copyLastSelected = mViewModel.getSelectedGpsPrediction();
                mViewModel.setSelectedGpsPrediction(getAdapterPosition());

                if (copyLastSelected == mViewModel.getSelectedGpsPrediction()) {
                    switch (mFragmentActivity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                        case Configuration.UI_MODE_NIGHT_YES:
                            card.setCardBackgroundColor(-15592942);
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                            card.setCardBackgroundColor(-1);
                            break;
                    }
                    card.setActivated(false);
                    mViewModel.setSelectedGpsPrediction(-1);
                    notifyItemChanged(copyLastSelected);

                    mViewModel.setField(FIELDS.NULL);
                } else {
                    card.setActivated(true);
                    card.setCardBackgroundColor(2130903247);
                    notifyItemChanged(copyLastSelected);
                    notifyItemChanged(mViewModel.getSelectedGpsPrediction());

                    mViewModel.setField(FIELDS.GPS_PREDICTION);
                }
            });
        }
    }
}
