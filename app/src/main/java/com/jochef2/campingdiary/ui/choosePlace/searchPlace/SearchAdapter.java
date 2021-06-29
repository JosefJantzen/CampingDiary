package com.jochef2.campingdiary.ui.choosePlace.searchPlace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jochef2.androidworlddata.World;
import com.jochef2.campingdiary.data.relations.FullPlace;
import com.jochef2.campingdiary.databinding.ItemPalceBinding;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;

import java.util.Comparator;
import java.util.Objects;

public class SearchAdapter extends SortedListAdapter<FullPlace> {

    private static FragmentActivity mFragmentActivity;
    private static ChoosePlaceViewModel mViewModel;

    public SearchAdapter(@NonNull Context context, @NonNull Comparator<FullPlace> comparator, FragmentActivity fragmentActivity) {
        super(context, FullPlace.class, comparator);
        mFragmentActivity = fragmentActivity;
        mViewModel = new ViewModelProvider(mFragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(mFragmentActivity.getApplication())).get(ChoosePlaceViewModel.class);
    }

    @NonNull
    @Override
    protected ViewHolder<? extends FullPlace> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final ItemPalceBinding binding = ItemPalceBinding.inflate(inflater, parent, false);
        return new SearchViewHolder(binding);
    }

    public static class SearchViewHolder extends SortedListAdapter.ViewHolder<FullPlace> {

        private final ItemPalceBinding mBinding;
        private final World mWorld;

        public SearchViewHolder(ItemPalceBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mWorld = new World(mFragmentActivity.getApplicationContext());
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        protected void performBind(@NonNull FullPlace item) {
            mBinding.setModel(item);
            if (item.mPlace.getAddressString() == null || item.mPlace.getAddressString().isEmpty()) {
                item.mPlace.generateAddressString();
            }
            mBinding.txPlace.setText(item.mPlace.getAddressString());
            mBinding.txDistance.setText(item.mPlace.getDistanceString());
            if (item.mPlace.getAddressObject() != null) {
                mBinding.imCountry.setImageDrawable(Objects.requireNonNull(mWorld.getCountry(item.mPlace.getAddressObject().getCountryCode())).getFlag(mFragmentActivity));
            }

            // resets autosize
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBinding.txName.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_NONE);
                mBinding.txName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                mBinding.txPlace.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_NONE);
                mBinding.txPlace.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mBinding.txName.post(() -> {
                    mBinding.txName.setAutoSizeTextTypeUniformWithConfiguration(12, 24, 1, TypedValue.COMPLEX_UNIT_SP);
                    mBinding.txPlace.setAutoSizeTextTypeUniformWithConfiguration(10, 18, 1, TypedValue.COMPLEX_UNIT_SP);
                });
            } else {
                TextViewCompat.setAutoSizeTextTypeWithDefaults(mBinding.txName, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
                mBinding.txName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(mBinding.txPlace, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
                mBinding.txPlace.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mBinding.txName.post(() -> {
                    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(mBinding.txName, 12, 24, 1, TypedValue.COMPLEX_UNIT_SP);
                    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(mBinding.txPlace, 10, 18, 1, TypedValue.COMPLEX_UNIT_SP);
                });
            }
        }
    }
}
