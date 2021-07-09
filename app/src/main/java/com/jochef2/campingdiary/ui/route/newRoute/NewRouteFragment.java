package com.jochef2.campingdiary.ui.route.newRoute;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Route;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewRouteFragment extends Fragment implements LifecycleObserver {

    private NewRouteViewModel mViewModel;

    private TextInputEditText etName;
    private TextView txTime;
    private MaterialButton btnTime;
    private MaterialButton btnDate;
    private TextInputEditText etMileage;
    private MaterialButton btnCancel;
    private MaterialButton btnDone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_route_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        txTime = view.findViewById(R.id.tx_name);
        btnTime = view.findViewById(R.id.btn_time);
        btnDate = view.findViewById(R.id.btn_date);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnDone = view.findViewById(R.id.btn_done);
        etMileage = view.findViewById(R.id.et_mileage);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
            etMileage.setText(savedInstanceState.getString("mileage"));
        }

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(NewRouteViewModel.class);
        mViewModel.setReiseId(NewRouteFragmentArgs.fromBundle(requireArguments()).getReiseId());

        mViewModel.mRoute.observe(getViewLifecycleOwner(), route -> {
            if (route != null) {
                txTime.setText(route.getTimeString());
            }
        });

        btnTime.setOnClickListener(v -> {
            Route route = mViewModel.mRoute.getValue();
            new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                route.mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                route.mTime.set(Calendar.MINUTE, minute);
                mViewModel.mRoute.setValue(route);

            }, route.getTime().get(Calendar.HOUR_OF_DAY), route.getTime().get(Calendar.MINUTE), true).show();
        });

        btnDate.setOnClickListener(v -> {
            Route route = mViewModel.mRoute.getValue();
            new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                route.mTime.set(year, month, dayOfMonth);
                mViewModel.mRoute.setValue(route);

            }, route.getTime().get(Calendar.YEAR), route.getTime().get(Calendar.MONTH), route.getTime().get(Calendar.DAY_OF_MONTH)).show();
        });

        InputMethodManager imgr = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btnCancel.setOnClickListener(v -> {
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            requireActivity().getViewModelStore().clear();
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        btnDone.setOnClickListener(v -> {
            save();
            mViewModel.saveRoute();
            requireActivity().getViewModelStore().clear();
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });
    }

    private void save() {
        Route route = mViewModel.mRoute.getValue();
        if (!etName.getText().toString().isEmpty()) {
            route.setName(etName.getText().toString());
        }
        if (!etMileage.getText().toString().isEmpty()) {
            route.setMileage(Double.parseDouble(etMileage.getText().toString()));
        }
        mViewModel.mRoute.setValue(route);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", Objects.requireNonNull(etName.getText()).toString());
        outState.putString("mileage", Objects.requireNonNull(etMileage.getText()).toString());
    }

    /**
     * Same as onActivityCreated
     * Set's title to R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.new_route);
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