package com.jochef2.campingdiary.ui.choosePlace.searchPlace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.databinding.ItemPalceBinding;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;

import java.util.Comparator;

public class SearchAdapter extends SortedListAdapter<Place> {

    private static FragmentActivity mFragmentActivity;
    private static ChoosePlaceViewModel mViewModel;
    private static Location mCurrentLocation;

    public SearchAdapter(@NonNull Context context, @NonNull Comparator<Place> comparator, FragmentActivity fragmentActivity) {
        super(context, Place.class, comparator);
        mFragmentActivity = fragmentActivity;
        mViewModel = new ViewModelProvider(mFragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(mFragmentActivity.getApplication())).get(ChoosePlaceViewModel.class);
        mViewModel.mCurrentLocation.observeForever(location -> mCurrentLocation = location);
    }

    @NonNull
    @Override
    protected ViewHolder<? extends Place> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final ItemPalceBinding binding = ItemPalceBinding.inflate(inflater, parent, false);
        return new SearchViewHolder(binding);
    }

    public static class SearchViewHolder extends SortedListAdapter.ViewHolder<Place> {

        private final ItemPalceBinding mBinding;

        public SearchViewHolder(ItemPalceBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void performBind(@NonNull Place item) {
            mBinding.setModel(item);
            mBinding.txDistance.setText(item.distanceTo(mCurrentLocation));
            if (item.getAddressObject() != null) {
                mBinding.imCountry.setImageResource(mFragmentActivity.getResources().getIdentifier(item.getAddressObject().getCountryCode().toLowerCase(), "drawable", "com.jochef2.countries"));

                if (item.getAddressObject().getThoroughfare() != null && item.getAddressObject().getSubThoroughfare() != null && item.getAddressObject().getLocality() != null) {
                    mBinding.txPlace.setText(item.getAddressObject().getThoroughfare() + " " + item.getAddressObject().getSubThoroughfare() + ", " + item.getAddressObject().getLocality());
                } else if (item.getAddressObject().getAddressLine(0) != null) {
                    mBinding.txPlace.setText(item.getAddressObject().getAddressLine(0));
                }
            } else if (item.getAddressString() != null) {
                mBinding.txPlace.setText(item.getAddressString());
            } else if (item.getCords() != null) {
                mBinding.txPlace.setText(item.getCordsString());
            }
        }
    }
}
