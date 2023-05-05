package com.josefjantzen.campingdiary.ui.events.newEvent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.josefjantzen.campingdiary.NavMainDirections;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.entities.Event;
import com.josefjantzen.campingdiary.data.values.Events;
import com.mynameismidori.currencypicker.CurrencyPicker;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NewEventThirdFragment extends Fragment {

    private TextInputEditText etName;
    private TextView txPlace;
    private MaterialButton btnChoosePlace;
    private TextInputEditText etPrice;
    private MaterialButton btnCurrency;
    private TextInputEditText etDescription;
    private MaterialButton btnBack;
    private MaterialButton btnDone;

    private CurrencyPicker mCurrencyPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        txPlace = view.findViewById(R.id.tx_place);
        btnChoosePlace = view.findViewById(R.id.btn_choose_place);
        etPrice = view.findViewById(R.id.et_price);
        btnCurrency = view.findViewById(R.id.btn_currency);
        etDescription = view.findViewById(R.id.et_description);
        btnBack = view.findViewById(R.id.btn_back);
        btnDone = view.findViewById(R.id.btn_done);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
            etPrice.setText(savedInstanceState.getString("price"));
            etDescription.setText(savedInstanceState.getString("description"));
        }

        NewEventFragment.mViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            btnCurrency.setText(event.getPrice().getCurrency().getCode());
        });

        NewEventFragment.mViewModel.mPlace.observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                txPlace.setText(place.mPlace.getPlaceName());
                if (etName.getText().toString().isEmpty()) {
                    etName.setText(place.mPlace.getPlaceName());
                }
            }
        });

        btnChoosePlace.setOnClickListener(v -> {
            NavMainDirections.ActionGlobalChoosePlaceFragment action = NavMainDirections.actionGlobalChoosePlaceFragment();
            action.setCat(Events.EVENT);
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(action);
        });

        mCurrencyPicker = CurrencyPicker.newInstance(getString(R.string.select_currency));

        btnCurrency.setOnClickListener(v -> {
            mCurrencyPicker.show(getChildFragmentManager(), "CURRENCY_PICKER");
        });

        mCurrencyPicker.setListener((name, code, symbol, slug, flagDrawableResID, pos) -> {
            btnCurrency.setText(code);
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            event.setCurrency(code);
            NewEventFragment.mViewModel.mEvent.setValue(event);
            mCurrencyPicker.dismiss();
        });

        InputMethodManager imgr = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btnBack.setOnClickListener(v -> {
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            NewEventFragment.setTab(1);
        });

        btnDone.setOnClickListener(v -> {
            save();
            NewEventFragment.mViewModel.saveEvent();
            NewEventFragment.navigateBack();
        });
    }

    private void save() {
        Event event = NewEventFragment.mViewModel.mEvent.getValue();
        if (!etName.getText().toString().isEmpty()) {
            event.setName(etName.getText().toString());
        }
        if (!etDescription.getText().toString().isEmpty()) {
            event.setDescription(etDescription.getText().toString());
        }
        if (!etPrice.getText().toString().isEmpty())
            event.setPriceNumber(Double.parseDouble(etPrice.getText().toString().replace(",", ".")));
        NewEventFragment.mViewModel.mEvent.setValue(event);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", Objects.requireNonNull(etName.getText()).toString());
        outState.putString("price", Objects.requireNonNull(etPrice.getText()).toString());
        outState.putString("description", Objects.requireNonNull(etDescription.getText()).toString());
    }
}