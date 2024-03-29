package com.josefjantzen.campingdiary.ui.reisen.currentReise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.entities.Event;
import com.josefjantzen.campingdiary.data.entities.Night;

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
    private MaterialCardView cardNight;
    private FloatingActionButton fabEvent;
    private TextView txEvent;
    private MaterialCardView cardEvent;
    private MaterialCardView cardFuel;
    private TextView txFuel;
    private TextView txPrice;
    private TextView txWater;
    private MaterialCardView cardWater;
    private TextView txCountries;
    private Button btnStop;
    private MaterialCardView cardRoute;
    private TextView txRoute;
    private MaterialCardView cardPrice;

    public static int mReiseId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_reise_fragment, container, false);
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
        cardNight = view.findViewById(R.id.card_night);
        fabEvent = view.findViewById(R.id.btn_event);
        txEvent = view.findViewById(R.id.tx_event);
        cardEvent = view.findViewById(R.id.card_event);
        cardFuel = view.findViewById(R.id.card_fuel);
        txFuel = view.findViewById(R.id.tx_fuel);
        txPrice = view.findViewById(R.id.tx_money);
        txWater = view.findViewById(R.id.tx_water);
        cardWater = view.findViewById(R.id.card_water);
        txCountries = view.findViewById(R.id.tx_countries);
        btnStop = view.findViewById(R.id.btn_stop);
        cardRoute = view.findViewById(R.id.card_route);
        txRoute = view.findViewById(R.id.tx_route);
        cardPrice = view.findViewById(R.id.card_price);

        mViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(CurrentReiseViewModel.class);

        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            // if there isn't a current reise saved
            if (reise == null) {
                reiseContainer.setVisibility(View.GONE);
                noReise.setVisibility(View.VISIBLE);
                btnStartReise.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_currentReiseFragment_to_newReisenFragment));

                //mViewModel.clearCurrent();
                /*NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.currentReiseFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_currentReiseFragment_to_allReisenFragment, savedInstanceState, navOptions);*/
                //Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_currentReiseFragment_to_allReisenFragment);
            }
            // if current reise is right initial views
            else {
                mReiseId = reise.mReise.getId();

                reiseContainer.setVisibility(View.VISIBLE);
                noReise.setVisibility(View.GONE);

                txDays.setText((TimeUnit.MILLISECONDS.toDays(Calendar.getInstance().getTimeInMillis() - reise.mReise.getBegin().getTimeInMillis()) + 1) + " / " + (TimeUnit.MILLISECONDS.toDays(reise.mReise.getEnd().getTimeInMillis() - reise.mReise.getBegin().getTimeInMillis()) + 1));
                fabNight.setOnClickListener(v -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToNewNightFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToNewNightFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(action);
                });

                fabEvent.setOnClickListener(v -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToNewEventFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToNewEventFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(action);

                });

                txPrice.setText(String.valueOf(reise.getTotalPrice()).replace(".", ",") + "€");

                if (!reise.mNights.isEmpty()) {
                    Night lastNight = reise.getLastNight();
                    txNight.setText(lastNight.getName());
                    cardNight.setOnClickListener(v -> {
                        CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToAllNightsFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToAllNightsFragment();
                        action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                        Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    });
                    //TODO: lastNight icon
                }

                if (!reise.mEvents.isEmpty()) {
                    Event lastEvent = reise.getLastEvent();
                    txEvent.setText(lastEvent.getName());
                    cardEvent.setOnClickListener(v -> {
                        CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToAllEventsFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToAllEventsFragment();
                        action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                        Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    });
                    //TODO: lastEvent icon
                }

                if (!reise.mFuels.isEmpty()) {
                    cardFuel.setOnClickListener(v -> {
                        CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToAllFuelsFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToAllFuelsFragment();
                        action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                        Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    });
                }
                txFuel.setText(String.valueOf(reise.getTotalFuel()).replace(".", ",") + "l");

                cardFuel.setOnLongClickListener(view1 -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToNewFuelFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToNewFuelFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    return true;
                });

                if (!reise.mSADs.isEmpty()) {
                    cardWater.setOnClickListener(v -> {
                        CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToAllSADsFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToAllSADsFragment();
                        action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                        Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    });
                }
                txWater.setText(String.valueOf(reise.getTotalWater()).replace(".", ",") + "l");

                cardWater.setOnLongClickListener(view1 -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToNewSADFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToNewSADFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    return true;
                });

                txCountries.setText(reise.getAllCountries());

                //TODO: Stop button wieder entfernen
                btnStop.setOnLongClickListener(v -> {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.attention))
                            .setMessage("Sind sie sich sicher, dass sie die Reise beenden wollen?")
                            .setIcon(android.R.drawable.stat_sys_warning)
                            .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                            .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                                reise.mReise.setEnd(Calendar.getInstance());
                                CurrentReiseViewModel.mReisenRepository.updateReise(reise.mReise);
                                dialog.dismiss();
                            })
                            .show();
                    return true;
                });

                txRoute.setText(reise.getTotalDistance() + "km");

                if (!reise.mRoutes.isEmpty()) {
                    cardRoute.setOnClickListener(v -> {
                        CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToAllRoutesFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToAllRoutesFragment();
                        action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                        Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    });
                }

                cardRoute.setOnLongClickListener(v -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToNewRouteFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToNewRouteFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                    return true;
                });

                cardPrice.setOnClickListener(view1 -> {
                    CurrentReiseFragmentDirections.ActionCurrentReiseFragmentToAllPricesFragment action = CurrentReiseFragmentDirections.actionCurrentReiseFragmentToAllPricesFragment();
                    action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
                    Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
                });
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
            if (reise != null) {
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