package com.josefjantzen.campingdiary.ui.places.choosePlace.newPlace;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.material.card.MaterialCardView;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel;
import com.josefjantzen.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel.FIELDS;

import java.util.List;

public class GooglePredictionAdapter extends RecyclerView.Adapter<GooglePredictionAdapter.ViewHolder> {

    private final ChoosePlaceViewModel mViewModel;

    private final List<PlaceLikelihood> mDataset;
    private final FragmentActivity mFragmentActivity;

    MaterialCardView cardView;
    ColorStateList defaultColor;

    public GooglePredictionAdapter(List<PlaceLikelihood> dataset, FragmentActivity fragmentActivity) {
        mDataset = dataset;
        mFragmentActivity = fragmentActivity;
        mViewModel = new ViewModelProvider(mFragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(mFragmentActivity.getApplication())).get(ChoosePlaceViewModel.class);

        cardView = new MaterialCardView(mFragmentActivity);
        defaultColor = cardView.getCardBackgroundColor();
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

        if (position == mViewModel.getSelectedGooglePrediction()) {
            holder.card.setCardBackgroundColor(R.attr.colorPrimaryVariant);
        } else {
            holder.card.setCardBackgroundColor(defaultColor);
        }
    }

    public void unselect() {
        int copy = mViewModel.getSelectedGooglePrediction();
        if (mViewModel.mField.getValue() != FIELDS.GOOGLE_PREDICTION) {
            mViewModel.setSelectedGooglePrediction(-1);
        }
        notifyItemChanged(copy);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txName;
        private final MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txName = itemView.findViewById(R.id.tx_name);
            card = itemView.findViewById(R.id.card);

            card.setOnClickListener(v -> {
                int copyLastSelected = mViewModel.getSelectedGooglePrediction();
                mViewModel.setSelectedGooglePrediction(getAdapterPosition());

                if (copyLastSelected == mViewModel.getSelectedGooglePrediction()) {
                    card.setCardBackgroundColor(defaultColor);
                    mViewModel.setSelectedGooglePrediction(-1);
                    notifyItemChanged(copyLastSelected);

                    mViewModel.setField(FIELDS.NULL);
                } else {
                    card.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                    notifyItemChanged(copyLastSelected);
                    notifyItemChanged(mViewModel.getSelectedGooglePrediction());

                    mViewModel.setField(FIELDS.GOOGLE_PREDICTION);
                }
            });
        }
    }
}
