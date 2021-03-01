package com.jochef2.campingdiary.ui.newNight;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Night;
import com.mynameismidori.currencypicker.CurrencyPicker;

import java.util.Calendar;
import java.util.Objects;

public class NewNightFirstFragment extends Fragment {

    private TextInputEditText etName;
    private ChipGroup chDuration;
    private TextInputEditText etPrice;
    private MaterialButton btnCurrency;
    private MaterialButton btnClose;
    private MaterialButton btnNext;
    private TextView txEnd;
    private Chip chCustom;

    private CurrencyPicker currencyPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_night_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        chDuration = view.findViewById(R.id.ch_end);
        etPrice = view.findViewById(R.id.et_price);
        btnCurrency = view.findViewById(R.id.btn_currency);
        btnClose = view.findViewById(R.id.btn_back);
        btnNext = view.findViewById(R.id.btn_check);
        txEnd = view.findViewById(R.id.tx_end);
        chCustom = view.findViewById(R.id.ch_custom);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
            etPrice.setText(savedInstanceState.getString("price"));
        }

        NewNightFragment.mViewModel.mNight.observe(getViewLifecycleOwner(), night -> {
            txEnd.setText(night.getEndDate());
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

        InputMethodManager imgr = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btnClose.setOnClickListener(v -> {
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            NewNightFragment.navigateBack();
        });

        btnNext.setOnClickListener(v -> {
            if (!etName.getText().toString().isEmpty()) {
                imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
                save();
                NewNightFragment.setTab(1);
            } else {
                Toast.makeText(getContext(), getString(R.string.err_no_name_event), Toast.LENGTH_SHORT).show();
            }
        });

        chDuration.setOnCheckedChangeListener((group, checkedId) -> {
            Night night = NewNightFragment.mViewModel.mNight.getValue();

            if (checkedId != R.id.ch_custom) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_WEEK, Integer.parseInt(view.findViewById(checkedId).getTag().toString()));
                night.setEnd(c);
                NewNightFragment.mViewModel.mNight.setValue(night);
                NewNightFragment.mViewModel.lastChip = checkedId;
            }
        });

        chCustom.setOnClickListener(v -> {
            Night night = NewNightFragment.mViewModel.mNight.getValue();
            Calendar c = night.getEnd();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                Calendar end = Calendar.getInstance();
                end.set(year, month, dayOfMonth);
                night.setEnd(end);
                NewNightFragment.mViewModel.mNight.setValue(night);
                NewNightFragment.mViewModel.lastChip = R.id.ch_custom;
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + 24 * 60 * 60 * 1000);
            datePickerDialog.setOnCancelListener(dialog -> chDuration.check(NewNightFragment.mViewModel.lastChip));
            datePickerDialog.show();
        });
    }

    private void save() {
        Night night = NewNightFragment.mViewModel.mNight.getValue();
        night.setName(etName.getText().toString());
        if (!etPrice.getText().toString().isEmpty())
            night.setPriceNumber(Double.parseDouble(etPrice.getText().toString()));
        NewNightFragment.mViewModel.mNight.setValue(night);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", Objects.requireNonNull(etName.getText()).toString());
        outState.putString("price", Objects.requireNonNull(etPrice.getText()).toString());
    }
}