package com.jochef2.campingdiary.ui.choosePlace.searchPlace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SearchPlaceFragment extends Fragment implements SearchView.OnQueryTextListener, SortedListAdapter.Callback {

    private static final Comparator<Place> COMPARATOR = new SortedListAdapter.ComparatorBuilder<Place>()
            .setOrderForModel(Place.class, (a, b) -> Integer.signum(a.mId - b.mId))
            .build();
    private ChoosePlaceViewModel mViewModel;
    private static SearchView mSearchView;
    private SearchAdapter mAdapter;
    private FragmentSearchPlaceBinding mBinding;
    private Animator mAnimator;

    private static boolean requestSearch = false;
    private String lastQuery = "";

    /**
     * filters  list of places for query
     *
     * @param places list of places to filter
     * @param query  folr filter
     * @return filterds list of places
     */
    private static List<Place> filter(List<Place> places, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Place> filteredModelList = new ArrayList<>();
        for (Place place : places) {
            final String name = place.getPlaceName().toLowerCase();
            String address = "";
            String country = "";

            if (place.getAddressString() != null) {
                address = place.getAddressString().toLowerCase();
            } else if (place.getAddressObject() != null) {
                if (place.getAddressObject().getAddressLine(0) != null) {
                    address = place.getAddressObject().getAddressLine(0);
                } else if (place.getAddressObject().getCountryName() != null) {
                    country = place.getAddressObject().getCountryName();
                }
            }

            if (name.contains(lowerCaseQuery) || address.contains(lowerCaseQuery) || country.contains(lowerCaseQuery)) {
                filteredModelList.add(place);
            }
        }
        return filteredModelList;
    }

    public static void search() {
        requestSearch = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_place_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.it_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setMaxWidth((int) (250 * getResources().getDisplayMetrics().density));
        if (requestSearch) {
            mSearchView.setIconified(false);
            requestSearch = false;
            if (!lastQuery.equals("")) {
                mSearchView.setIconified(false);
                mSearchView.setQuery(lastQuery, false);
            }
        } else if (!lastQuery.equals("")) {
            mSearchView.setIconified(false);
            mSearchView.setQuery(lastQuery, true);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_place, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);

        mAdapter = new SearchAdapter(requireContext(), COMPARATOR, requireActivity());
        mAdapter.addCallback(this);


        mBinding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.recycler.setAdapter(mAdapter);

        mViewModel.getAllPlaces().observe(getViewLifecycleOwner(), places -> {
            mAdapter.edit()
                    .replaceAll(places)
                    .commit();
        });

        mViewModel.mCurrentLocation.observe(getViewLifecycleOwner(), location -> mAdapter.notifyDataSetChanged());
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
        lastQuery = query;
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