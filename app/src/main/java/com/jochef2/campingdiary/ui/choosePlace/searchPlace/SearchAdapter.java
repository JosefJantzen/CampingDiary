package com.jochef2.campingdiary.ui.choosePlace.searchPlace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.databinding.ItemPalceBinding;

import java.util.Comparator;

public class SearchAdapter extends SortedListAdapter<Place> {


    public SearchAdapter(@NonNull Context context, @NonNull Comparator<Place> comparator) {
        super(context, Place.class, comparator);
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

        @Override
        protected void performBind(@NonNull Place item) {
            mBinding.setModel(item);
        }
    }
}
