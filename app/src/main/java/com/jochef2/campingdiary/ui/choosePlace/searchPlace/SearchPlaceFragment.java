package com.jochef2.campingdiary.ui.choosePlace.searchPlace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Place;
import com.jochef2.campingdiary.databinding.FragmentSearchPlaceBinding;
import com.jochef2.campingdiary.ui.choosePlace.ChoosePlaceViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SearchPlaceFragment extends Fragment implements SearchView.OnQueryTextListener, SortedListAdapter.Callback {

    private static final Comparator<Place> COMPARATOR = new SortedListAdapter.ComparatorBuilder<Place>()
            .setOrderForModel(Place.class, (a, b) -> Integer.signum(a.mId - b.mId))
            .build();
    private ChoosePlaceViewModel mViewModel;
    private SearchView mSearchView;
    private SearchAdapter mAdapter;
    private FragmentSearchPlaceBinding mBinding;
    private Animator mAnimator;

    private static List<Place> filter(List<Place> places, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Place> filteredModelList = new ArrayList<>();
        for (Place place : places) {
            final String name = place.getPlaceName().toLowerCase();
            String address = "";
            if (place.getAddressString() != null) {
                address = place.getAddressString().toLowerCase();
            }

            if (name.contains(lowerCaseQuery) || address.contains(lowerCaseQuery)) {
                filteredModelList.add(place);
            }
        }
        return filteredModelList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_place, container, false);
        View view = mBinding.getRoot();

        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);


        mSearchView = view.findViewById(R.id.search);
        mSearchView.setOnClickListener(v -> mSearchView.onActionViewExpanded());
        mSearchView.setOnQueryTextListener(this);

        mAdapter = new SearchAdapter(requireContext(), COMPARATOR);
        mAdapter.addCallback(this);


        mBinding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.recycler.setAdapter(mAdapter);

        mViewModel.getAllPlaces().observe(getViewLifecycleOwner(), places -> {
            mAdapter.edit()
                    .replaceAll(places)
                    .commit();
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Place> filteredPlaceList = filter(Objects.requireNonNull(mViewModel.getAllPlaces().getValue()), query);
        mAdapter.edit()
                .replaceAll(filteredPlaceList)
                .commit();
        return true;
    }

    @Override
    public void onEditStarted() {
        if (mBinding.progressBar.getVisibility() != View.VISIBLE) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            mBinding.progressBar.setAlpha(0.0f);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.progressBar, View.ALPHA, 1.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();

        mBinding.recycler.animate().alpha(0.5f);
    }

    @Override
    public void onEditFinished() {
        mBinding.recycler.scrollToPosition(0);
        mBinding.recycler.animate().alpha(1.0f);

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.progressBar, View.ALPHA, 0.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mCanceled) {
                    mBinding.progressBar.setVisibility(View.GONE);
                }
            }
        });
        mAnimator.start();
    }
}