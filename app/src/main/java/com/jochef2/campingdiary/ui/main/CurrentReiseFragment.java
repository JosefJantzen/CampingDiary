package com.jochef2.campingdiary.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Night;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CurrentReiseFragment extends Fragment implements LifecycleObserver {

    private CurrentReiseViewModel mViewModel;

    private ConstraintLayout reiseContainer;
    private LinearLayout noReise;
    private MaterialButton btnStartReise;
    private TextView txDays;
    private FloatingActionButton fabNight;
    private TextView txNight;
    private ImageView imNight;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_reise_fragment, container, false);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reiseContainer = view.findViewById(R.id.container_reise);
        noReise = view.findViewById(R.id.no_reise);
        btnStartReise = view.findViewById(R.id.btn_start_reise);
        txDays = view.findViewById(R.id.tx_days);
        fabNight = view.findViewById(R.id.btn_nights);
        txNight = view.findViewById(R.id.tx_night);
        imNight = view.findViewById(R.id.im_night);

        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(CurrentReiseViewModel.class);

        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {

            // if there isn't a current reise saved
            if (new Gson().toJson(reise).equals("{}") || reise.mReise.getEnd().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                reiseContainer.setVisibility(View.GONE);
                noReise.setVisibility(View.VISIBLE);
                btnStartReise.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_currentReiseFragment_to_newReisenFragment));

                mViewModel.clearCurrent();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.currentReiseFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_currentReiseFragment_to_allReisenFragment, savedInstanceState, navOptions);
            }
            // if current reise is right initial views
            else {
                reiseContainer.setVisibility(View.VISIBLE);
                noReise.setVisibility(View.GONE);

                txDays.setText((TimeUnit.MILLISECONDS.toDays(Calendar.getInstance().getTimeInMillis() - reise.mReise.getBegin().getTimeInMillis()) + 1) + " / " + (TimeUnit.MILLISECONDS.toDays(reise.mReise.getEnd().getTimeInMillis() - reise.mReise.getBegin().getTimeInMillis()) + 1));
                fabNight.setOnClickListener(v -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToNewNightFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToNewNightFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                });

                Night lastNight = reise.mNights.get(reise.mNights.size() - 1);
                txNight.setText(lastNight.getName());
                //TODO: lastNight icon
            }
        });
    }

    /**
     * same as onActivityCreated
     * sets title either to reise.name or R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            if (!new Gson().toJson(reise).equals("{}")) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(reise.mReise.getName());
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.start_reise));
            }
        });
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