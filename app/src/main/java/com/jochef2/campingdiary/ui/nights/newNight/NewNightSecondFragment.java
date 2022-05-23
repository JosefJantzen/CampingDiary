package com.jochef2.campingdiary.ui.nights.newNight;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.values.NightCategory;
import com.mynameismidori.currencypicker.CurrencyPicker;

import java.util.Objects;

public class NewNightSecondFragment extends Fragment {

    private ChipGroup chCategory;
    private TextInputEditText etDescription;
    private MaterialButton btnBack;
    private MaterialButton btnCheck;
    private TextInputEditText etPrice;
    private MaterialButton btnCurrency;

    private CurrencyPicker currencyPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_night_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chCategory = view.findViewById(R.id.ch_category);
        etDescription = view.findViewById(R.id.et_description);
        btnBack = view.findViewById(R.id.btn_back);
        btnCheck = view.findViewById(R.id.btn_check);
        etPrice = view.findViewById(R.id.et_price);
        btnCurrency = view.findViewById(R.id.btn_currency);

        if (savedInstanceState != null) {
            etDescription.setText(savedInstanceState.getString("description"));
            etPrice.setText(savedInstanceState.getString("price"));
        }

        NewNightFragment.mViewModel.mNight.observe(getViewLifecycleOwner(), night -> {
            btnCurrency.setText(night.getPrice().getCurrency().getCode());
        });

        currencyPicker = CurrencyPicker.newInstance(getString(R.string.select_currency));

        btnCurrency.setOnClickListener(v -> {
            currencyPicker.show(getChildFragmentManager(), "CURRENCY_PICKER");
        });

        currencyPicker.setListener((name, code, symbol, slug, flagDrawableResID, pos) -> {
            btnCurrency.setText(code);
            Night night = NewNightFragment.mViewModel.mNight.getValue();
            night.setCurrency(code);
            NewNightFragment.mViewModel.mNight.setValue(night);
            currencyPicker.dismiss();
        });

        chCategory.setOnCheckedChangeListener(((group, checkedId) -> {
            Night night = NewNightFragment.mViewModel.mNight.getValue();
            night.setCat(NightCategory.valueOf(view.findViewById(checkedId).getTag().toString()));
            NewNightFragment.mViewModel.mNight.setValue(night);
        }));

        InputMethodManager imgr = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btnBack.setOnClickListener(v -> {
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            save();
            NewNightFragment.setTab(0);
        });

        btnCheck.setOnClickListener(v -> {
            save();
            NewNightFragment.mViewModel.saveNight();
            NewNightFragment.navigateBack();
        });
    }

    private void save() {
        Night night = NewNightFragment.mViewModel.mNight.getValue();
        if (!etDescription.getText().toString().isEmpty()) {
            night.setDescription(etDescription.getText().toString());
        }
        if (!etPrice.getText().toString().isEmpty()) {
            night.setPriceNumber(Double.parseDouble(etPrice.getText().toString().replace(",", ".")));
        }
        NewNightFragment.mViewModel.mNight.setValue(night);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("description", Objects.requireNonNull(etDescription.getText().toString()));
        outState.putString("price", Objects.requireNonNull(etPrice.getText()).toString());
    }
}