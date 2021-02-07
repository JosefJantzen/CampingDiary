package com.jochef2.campingdiary.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.jochef2.campingdiary.R;

public class CurrentReiseFragment extends Fragment implements LifecycleObserver {

    private CurrentReiseViewModel mViewModel;

    private ConstraintLayout reiseContainer;
    private LinearLayout noReise;
    private MaterialButton btnStartReise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_reise_fragment, container, false);

        reiseContainer = view.findViewById(R.id.container_reise);
        noReise = view.findViewById(R.id.no_reise);
        btnStartReise = view.findViewById(R.id.btn_start_reise);

        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(CurrentReiseViewModel.class);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            if (reise == null) {
                reiseContainer.setVisibility(View.GONE);
                noReise.setVisibility(View.VISIBLE);

                btnStartReise.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_currentReiseFragment_to_newReisenFragment));
            } else {
                reiseContainer.setVisibility(View.VISIBLE);
                noReise.setVisibility(View.GONE);
            }
        });


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(reise.mReise.getName());
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getLifecycle().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getLifecycle().removeObserver(this);
    }
}