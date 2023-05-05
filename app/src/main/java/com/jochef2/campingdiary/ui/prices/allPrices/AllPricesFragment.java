package com.jochef2.campingdiary.ui.prices.allPrices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.nights.allNights.AllNightsAdapter;
import com.jochef2.campingdiary.ui.nights.allNights.AllNightsFragmentArgs;
import com.jochef2.campingdiary.ui.nights.allNights.AllNightsViewModel;
import com.jochef2.campingdiary.ui.nights.allNights.AllNightsViewModelFactory;

import java.util.Objects;

public class AllPricesFragment extends Fragment implements LifecycleObserver {

    private AllPricesViewModel mViewModel;

    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_prices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);

        mViewModel = new ViewModelProvider(requireActivity(), new AllPricesViewModelFactory(getActivity().getApplication(), AllNightsFragmentArgs.fromBundle(requireArguments()).getReiseId())).get(AllPricesViewModel.class);

        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            AllPricesAdapter adapter = new AllPricesAdapter(mViewModel.mReise, getContext(), getChildFragmentManager());
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        });
    }

    /**
     * same as onActivityCreated
     * sets title either to reise.name or R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(getString(R.string.prices));
    }

    /**
     * for onActivityCreated
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getLifecycle().addObserver(this);
    }

    /**
     * for onActivityCreated
     */
    @Override
    public void onDetach() {
        super.onDetach();
        getLifecycle().removeObserver(this);
    }
}