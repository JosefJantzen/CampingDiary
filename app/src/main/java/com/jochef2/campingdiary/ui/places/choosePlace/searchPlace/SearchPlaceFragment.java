package com.jochef2.campingdiary.ui.places.choosePlace.searchPlace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.material.button.MaterialButton;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.relations.FullPlace;
import com.jochef2.campingdiary.data.values.PlaceSortBy;
import com.jochef2.campingdiary.databinding.FragmentSearchPlaceBinding;
import com.jochef2.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SearchPlaceFragment extends Fragment implements SortedListAdapter.Callback {

    /**
     * Comparator for different SortBy
     */
    private static final Comparator<FullPlace> COMPARATOR_CREATION_DATE = new SortedListAdapter.ComparatorBuilder<FullPlace>()
            .setOrderForModel(FullPlace.class, (a, b) -> Integer.signum(a.mPlace.mId - b.mPlace.mId))
            .build().reversed();

    private static final Comparator<FullPlace> COMPARATOR_NAME = new SortedListAdapter.ComparatorBuilder<FullPlace>()
            .setOrderForModel(FullPlace.class, (o1, o2) -> o1.mPlace.getPlaceName().compareTo(o2.mPlace.mPlaceName))
            .build().thenComparing(COMPARATOR_CREATION_DATE);

    private static final Comparator<FullPlace> COMPARATOR_ADDRESS = new SortedListAdapter.ComparatorBuilder<FullPlace>()
            .setOrderForModel(FullPlace.class, (o1, o2) -> o1.mPlace.getAddressString().compareTo(o2.mPlace.getAddressString()))
            .build().thenComparing(COMPARATOR_NAME);

    private static final Comparator<FullPlace> COMPARATOR_DISTANCE = new SortedListAdapter.ComparatorBuilder<FullPlace>()
            .setOrderForModel(FullPlace.class, (o1, o2) -> Double.compare(o1.mPlace.getDistance(), o2.mPlace.getDistance()))
            .build().thenComparing(COMPARATOR_NAME);

    private static final Comparator<FullPlace> COMPARATOR_NUMBER_OF_VISITS = new SortedListAdapter.ComparatorBuilder<FullPlace>()
            .setOrderForModel(FullPlace.class, (o1, o2) -> Integer.signum(o1.getNumberOfVisits() - o2.getNumberOfVisits()))
            .build().reversed();

    private static final Comparator<FullPlace> COMPARATOR_LAST_VISITED = new SortedListAdapter.ComparatorBuilder<FullPlace>()
            .setOrderForModel(FullPlace.class, (o1, o2) -> Long.compare(o1.getLastEventDateInMillis(), o2.getLastEventDateInMillis()))
            .build().reversed();

    private static ChoosePlaceViewModel mViewModel;
    private static SearchAdapter mAdapter;
    private FragmentSearchPlaceBinding mBinding;
    private Animator mAnimator;

    private static boolean requestSearch = false;
    private String lastQuery = "";

    private SearchView mSearchView;
    private AutoCompleteTextView txSortBy;
    private MaterialButton btnSort;

    private final AtomicBoolean distances = new AtomicBoolean(false);

    private FilterDialogFragment mFilterDialogFragment;

    /**
     * sets requestSearch to true -> needed for onCreateOptionsMenu
     */
    public static void search() {
        requestSearch = true;
    }

    /**
     * filters  list of places for query
     *
     * @param query for filter
     * @return filters list of places
     */
    private void searchFilter(String query) {
        List<FullPlace> places = mViewModel.getAllPlaces().getValue();
        if (places != null) {
            final String lowerCaseQuery = query.toLowerCase();

            final List<FullPlace> filteredModelList = new ArrayList<>();
            for (FullPlace place : places) {
                final String name = place.mPlace.getPlaceName().toLowerCase();
                String address = "";
                String locality = "";
                String subLocality = "";
                String thoroughfare = "";
                String subThoroughfare = "";
                String premises = "";
                String postalCode = "";
                String country = "";
                String phone = "";
                String url = "";

                if (place.mPlace.getAddressString() != null) {
                    address = place.mPlace.getAddressString().toLowerCase();
                } else if (place.mPlace.getAddressObject() != null) {
                    if (place.mPlace.getAddressObject().getAddressLine(0) != null) {
                        address = place.mPlace.getAddressObject().getAddressLine(0);
                    } else {
                        if (place.mPlace.getAddressObject().getLocality() != null) {
                            locality = place.mPlace.getAddressObject().getLocality();
                        }
                        if (place.mPlace.getAddressObject().getSubLocality() != null) {
                            subLocality = place.mPlace.getAddressObject().getSubLocality();
                        }
                        if (place.mPlace.getAddressObject().getThoroughfare() != null) {
                            thoroughfare = place.mPlace.getAddressObject().getThoroughfare();
                        }
                        if (place.mPlace.getAddressObject().getSubThoroughfare() != null) {
                            subThoroughfare = place.mPlace.getAddressObject().getSubThoroughfare();
                        }
                        if (place.mPlace.getAddressObject().getPremises() != null) {
                            premises = place.mPlace.getAddressObject().getPremises();
                        }
                        if (place.mPlace.getAddressObject().getPostalCode() != null) {
                            postalCode = place.mPlace.getAddressObject().getPostalCode();
                        }
                        if (place.mPlace.getAddressObject().getCountryName() != null) {
                            country = place.mPlace.getAddressObject().getCountryName();
                        }
                    }
                    if (place.mPlace.getAddressObject().getPhone() != null) {
                        phone = place.mPlace.getAddressObject().getPhone();
                    }
                    if (place.mPlace.getAddressObject().getUrl() != null) {
                        url = place.mPlace.getAddressObject().getUrl();
                    }
                }

                if (name.contains(lowerCaseQuery) || address.contains(lowerCaseQuery)
                        || locality.contains(lowerCaseQuery) || subLocality.contains(lowerCaseQuery)
                        || thoroughfare.contains(lowerCaseQuery) || subThoroughfare.contains(lowerCaseQuery)
                        || premises.contains(lowerCaseQuery) || postalCode.contains(lowerCaseQuery)
                        || country.contains(lowerCaseQuery) || phone.contains(lowerCaseQuery)
                        || url.contains(lowerCaseQuery)) {
                    filteredModelList.add(place);
                }
            }
            sort(filteredModelList);
        }
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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mViewModel.mSearchQuery.setValue(query);
                lastQuery = query;
                return true;
            }
        });

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

        MenuItem filterItem = menu.findItem(R.id.it_filter);
        filterItem.setOnMenuItemClickListener(item -> {
            mFilterDialogFragment = new FilterDialogFragment(getMinAndMaxDistance(mViewModel.getAllPlaces().getValue()));
            mFilterDialogFragment.setListener(new FilterDialogFragment.PlaceFilterListener() {
                @Override
                public void onResult(float distanceMin, float distanceMax) {
                    Log.d("TAG", "min: " + distanceMin + " max: " + distanceMax);
                }

                @Override
                public void onCancel() {

                }
            });
            mFilterDialogFragment.show(getChildFragmentManager(), "SEARCH_PLACE_FILTER");
            return true;
        });
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

        txSortBy = view.findViewById(R.id.sort_by);
        btnSort = view.findViewById(R.id.btn_sort);

        mAdapter = new SearchAdapter(requireContext(), COMPARATOR_CREATION_DATE, requireActivity());
        mAdapter.addCallback(this);

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.recycler.setAdapter(mAdapter);

        /**
         * sets distances for places
         */
        mViewModel.getAllPlaces().observe(getViewLifecycleOwner(), places -> {
            if (!distances.get() && places.get(0).mPlace.getDistance() == -1) {
                List<Boolean> success = new ArrayList<>();
                for (FullPlace place : places) {
                    success.add(place.mPlace.setDistanceTo(mViewModel.mCurrentLocation.getValue()));
                }
                if (success.contains(true)) distances.set(true);
            }
            searchFilter(mViewModel.mSearchQuery.getValue());
        });

        mViewModel.mCurrentLocation.observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                List<FullPlace> places = mViewModel.getAllPlaces().getValue();
                if (places != null) {
                    List<Boolean> success = new ArrayList<>();
                    for (FullPlace place : places) {
                        success.add(place.mPlace.setDistanceTo(mViewModel.mCurrentLocation.getValue()));
                    }
                    if (success.contains(true)) distances.set(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        // initialize SortBy View
        List<String> sortByStrings = Arrays.asList(getResources().getStringArray(R.array.sort_by_values));
        ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.list_item, sortByStrings);
        txSortBy.setAdapter(adapter);
        txSortBy.setText(adapter.getItem(0).toString(), false);

        txSortBy.setOnItemClickListener((parent, view1, position, id) -> {
            PlaceSortBy placeSortBy = PlaceSortBy.values()[position];
            mViewModel.mPlaceSortBy.setValue(placeSortBy);
        });

        btnSort.setOnClickListener(v -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recycler.getLayoutManager();
            if (Objects.requireNonNull(layoutManager).getReverseLayout()) {
                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);
            } else {
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
            }
            mBinding.recycler.setLayoutManager(layoutManager);
        });

        // calls filter on search query change
        mViewModel.mSearchQuery.observe(getViewLifecycleOwner(), this::searchFilter);

        // sorts places when SortBy changes
        mViewModel.mPlaceSortBy.observe(getViewLifecycleOwner(), placeSortBy ->
                sort(mViewModel.mShownPlaces.getValue()));
    }

    /**
     * Callback from SearchAdapter when edit in SearchView starts
     * handles progressBar animations
     */
    @Override
    public void onEditStarted() {
        if (mBinding.progressSearchPlace.getVisibility() != View.VISIBLE) {
            mBinding.progressSearchPlace.setVisibility(View.VISIBLE);
            mBinding.progressSearchPlace.setAlpha(0.0f);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.progressSearchPlace, View.ALPHA, 1.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();

        mBinding.recycler.animate().alpha(0.5f);
    }

    /**
     * Callback from SearchAdapter when edit in SearchView finish
     * handles progressBar animations
     */
    @Override
    public void onEditFinished() {
        mBinding.recycler.scrollToPosition(0);
        mBinding.recycler.animate().alpha(1.0f);

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.progressSearchPlace, View.ALPHA, 0.0f);
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
                    mBinding.progressSearchPlace.setVisibility(View.GONE);
                }
            }
        });
        mAnimator.start();
    }

    /**
     * Sets the adapter or the recycler based on the given SortBy selection and replaces all places
     *
     * @param places filtered List of Place for the recycler
     */
    private void sort(List<FullPlace> places) {
        if (places != null && mViewModel.mPlaceSortBy.getValue() != null) {
            switch (mViewModel.mPlaceSortBy.getValue()) {
                case NAME:
                    mAdapter = new SearchAdapter(requireContext(), COMPARATOR_NAME, requireActivity());
                    break;
                case ADDRESS:
                    mAdapter = new SearchAdapter(requireContext(), COMPARATOR_ADDRESS, requireActivity());
                    break;
                case DISTANCE:
                    mAdapter = new SearchAdapter(requireContext(), COMPARATOR_DISTANCE, requireActivity());
                    break;
                case CREATION_DATE:
                    mAdapter = new SearchAdapter(requireContext(), COMPARATOR_CREATION_DATE, requireActivity());
                    break;
                case NUMBER_OF_VISITS:
                    mAdapter = new SearchAdapter(requireContext(), COMPARATOR_NUMBER_OF_VISITS, requireActivity());
                    break;
                case LAST_VISITED:
                    mAdapter = new SearchAdapter(requireContext(), COMPARATOR_LAST_VISITED, requireActivity());
                    break;
            }
            mBinding.recycler.swapAdapter(mAdapter, true);
            mAdapter.edit()
                    .replaceAll(places)
                    .commit();
            mViewModel.mShownPlaces.setValue(places);
        }
    }

    private List<Float> getMinAndMaxDistance(List<FullPlace> places) {
        if (places != null && distances.get() && places.size() > 1) {
            places.sort(COMPARATOR_DISTANCE);
            List<Float> distances = new ArrayList<>();
            distances.add(0, (float) places.get(0).mPlace.getDistance());
            distances.add(1, (float) places.get(places.size() - 1).mPlace.getDistance());
            if (!distances.contains(null)) {
                return distances;
            }
        }
        return null;
    }
}