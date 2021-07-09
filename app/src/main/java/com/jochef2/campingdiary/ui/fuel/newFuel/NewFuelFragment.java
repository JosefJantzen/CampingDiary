package com.jochef2.campingdiary.ui.fuel.newFuel;

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
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.NavMainDirections;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Fuel;
import com.jochef2.campingdiary.data.values.Events;
import com.mynameismidori.currencypicker.CurrencyPicker;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewFuelFragment extends Fragment implements LifecycleObserver {

    private NewFuelViewModel mViewModel;
    private CurrencyPicker mCurrencyPicker;
    private boolean mIsWholePrice = true;

    private TextInputEditText etName;
    private TextView txTime;
    private MaterialButton btnTime;
    private MaterialButton btnDate;
    private TextView txPlace;
    private MaterialButton btnChoosePlace;
    private TextInputEditText etAmount;
    private TextInputEditText etPrice;
    private MaterialButton btnCurrency;
    private MaterialButtonToggleGroup mTooglePrice;
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
        return inflater.inflate(R.layout.new_fuel_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        txTime = view.findViewById(R.id.tx_time);
        btnTime = view.findViewById(R.id.btn_time);
        btnDate = view.findViewById(R.id.btn_date);
        txPlace = view.findViewById(R.id.tx_place);
        btnChoosePlace = view.findViewById(R.id.btn_place);
        etAmount = view.findViewById(R.id.et_amount);
        etPrice = view.findViewById(R.id.et_price);
        btnCurrency = view.findViewById(R.id.btn_currency);
        mTooglePrice = view.findViewById(R.id.toggle_price);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnDone = view.findViewById(R.id.btn_done);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
            etAmount.setText(savedInstanceState.getString("amount"));
            etPrice.setText(savedInstanceState.getString("price"));
        }

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(NewFuelViewModel.class);
        mViewModel.setReiseId(NewFuelFragmentArgs.fromBundle(requireArguments()).getReiseId());

        mViewModel.mPlace.observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                txPlace.setText(place.mPlace.getPlaceName());
                if (etName.getText().toString().isEmpty()) {
                    etName.setText(place.mPlace.getPlaceName());
                }
            }
        });

        mViewModel.mFuel.observe(getViewLifecycleOwner(), fuel -> {
            if (fuel != null) {
                txTime.setText(fuel.getTimeString());
            }
        });

        btnTime.setOnClickListener(v -> {
            Fuel fuel = mViewModel.mFuel.getValue();
            new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                fuel.mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                fuel.mTime.set(Calendar.MINUTE, minute);
                mViewModel.mFuel.setValue(fuel);

            }, fuel.getTime().get(Calendar.HOUR_OF_DAY), fuel.getTime().get(Calendar.MINUTE), true).show();
        });

        btnDate.setOnClickListener(v -> {
            Fuel fuel = mViewModel.mFuel.getValue();
            new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                fuel.mTime.set(year, month, dayOfMonth);
                mViewModel.mFuel.setValue(fuel);

            }, fuel.getTime().get(Calendar.YEAR), fuel.getTime().get(Calendar.MONTH), fuel.getTime().get(Calendar.DAY_OF_MONTH)).show();
        });

        btnChoosePlace.setOnClickListener(v -> {
            NavMainDirections.ActionGlobalChoosePlaceFragment action = NavMainDirections.actionGlobalChoosePlaceFragment();
            action.setCat(Events.FUEL);
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(action);
        });

        mCurrencyPicker = CurrencyPicker.newInstance(getString(R.string.select_currency));

        btnCurrency.setOnClickListener(v -> {
            mCurrencyPicker.show(getChildFragmentManager(), "CURRENCY_PICKER");
        });

        mCurrencyPicker.setListener((name, code, symbol, slug, flagDrawableResID, pos) -> {
            btnCurrency.setText(code);
            Fuel fuel = mViewModel.mFuel.getValue();
            fuel.setCurrency(code);
            mViewModel.mFuel.setValue(fuel);
            mCurrencyPicker.dismiss();
        });

        mTooglePrice.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.btn_whole && isChecked) {
                mIsWholePrice = true;
            }
            else if (checkedId == R.id.btn_liter && isChecked) {
                mIsWholePrice = false;
            }
        });

        InputMethodManager imgr = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btnCancel.setOnClickListener(v -> {
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            requireActivity().getViewModelStore().clear();
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        btnDone.setOnClickListener(v -> {
            save();
            mViewModel.saveFuel();
            requireActivity().getViewModelStore().clear();
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });
    }

    private void save() {
        Fuel fuel = mViewModel.mFuel.getValue();
        if (!etName.getText().toString().isEmpty()) {
            fuel.setName(etName.getText().toString());
        }
        if (!etAmount.getText().toString().isEmpty()) {
            fuel.setLiter(Double.parseDouble(etAmount.getText().toString().replace(",", ".")));
        }
        if (!etPrice.getText().toString().isEmpty()) {
            double price = Double.parseDouble(etPrice.getText().toString().replace(",", "."));
            if (!mIsWholePrice) {
                price = price * fuel.getLiter();
            }
            fuel.setPriceNumber(price);
        }
        mViewModel.mFuel.setValue(fuel);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", Objects.requireNonNull(etName.getText()).toString());
        outState.putString("amount", Objects.requireNonNull(etAmount.getText()).toString());
        outState.putString("price", Objects.requireNonNull(etPrice.getText()).toString());
    }

    /**
     * Same as onActivityCreated
     * Set's title to R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.new_fuel);
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