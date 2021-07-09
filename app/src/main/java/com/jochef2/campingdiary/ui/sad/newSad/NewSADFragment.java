package com.jochef2.campingdiary.ui.sad.newSad;

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
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.NavMainDirections;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.SupplyAndDisposal;
import com.jochef2.campingdiary.data.values.Events;
import com.jochef2.campingdiary.data.values.SADCategory;
import com.jochef2.campingdiary.ui.fuel.newFuel.NewFuelFragmentArgs;
import com.mynameismidori.currencypicker.CurrencyPicker;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewSADFragment extends Fragment implements LifecycleObserver {

    private NewSADViewModel mViewModel;
    private CurrencyPicker mCurrencyPicker;

    private TextInputEditText etName;
    private TextView txTime;
    private MaterialButton btnTime;
    private MaterialButton btnDate;
    private TextView txPlace;
    private MaterialButton btnChoosePlace;
    private ChipGroup cgCat;
    private TextInputEditText etWater;
    private TextInputEditText etPrice;
    private MaterialButton btnCurrency;
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
        return inflater.inflate(R.layout.new_sad_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        txTime = view.findViewById(R.id.tx_name);
        btnTime = view.findViewById(R.id.btn_time);
        btnDate = view.findViewById(R.id.btn_date);
        txPlace = view.findViewById(R.id.tx_place);
        btnChoosePlace = view.findViewById(R.id.btn_place);
        cgCat = view.findViewById(R.id.cg_cat);
        etWater = view.findViewById(R.id.et_water);
        etPrice = view.findViewById(R.id.et_price);
        btnCurrency = view.findViewById(R.id.btn_currency);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnDone = view.findViewById(R.id.btn_done);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
            etWater.setText(savedInstanceState.getString("water"));
            etPrice.setText(savedInstanceState.getString("price"));
        }

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(NewSADViewModel.class);
        mViewModel.setReiseId(NewFuelFragmentArgs.fromBundle(requireArguments()).getReiseId());

        mViewModel.mPlace.observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                txPlace.setText(place.mPlace.getPlaceName());
                if (etName.getText().toString().isEmpty()) {
                    etName.setText(place.mPlace.getPlaceName());
                }
            }
        });

        mViewModel.mSAD.observe(getViewLifecycleOwner(), sad -> {
            if (sad != null) {
                txTime.setText(sad.getTimeString());
            }
        });

        btnTime.setOnClickListener(v -> {
            SupplyAndDisposal sad = mViewModel.mSAD.getValue();
            new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                sad.mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                sad.mTime.set(Calendar.MINUTE, minute);
                mViewModel.mSAD.setValue(sad);

            }, sad.getTime().get(Calendar.HOUR_OF_DAY), sad.getTime().get(Calendar.MINUTE), true).show();
        });

        btnDate.setOnClickListener(v -> {
            SupplyAndDisposal sad = mViewModel.mSAD.getValue();
            new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                sad.mTime.set(year, month, dayOfMonth);
                mViewModel.mSAD.setValue(sad);

            }, sad.getTime().get(Calendar.YEAR), sad.getTime().get(Calendar.MONTH), sad.getTime().get(Calendar.DAY_OF_MONTH)).show();
        });

        btnChoosePlace.setOnClickListener(v -> {
            NavMainDirections.ActionGlobalChoosePlaceFragment action = NavMainDirections.actionGlobalChoosePlaceFragment();
            action.setCat(Events.SAD);
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(action);
        });

        mCurrencyPicker = CurrencyPicker.newInstance(getString(R.string.select_currency));

        btnCurrency.setOnClickListener(v -> {
            mCurrencyPicker.show(getChildFragmentManager(), "CURRENCY_PICKER");
        });

        mCurrencyPicker.setListener((name, code, symbol, slug, flagDrawableResID, pos) -> {
            btnCurrency.setText(code);
            SupplyAndDisposal sad = mViewModel.mSAD.getValue();
            sad.setCurrency(code);
            mViewModel.mSAD.setValue(sad);
            mCurrencyPicker.dismiss();
        });

        InputMethodManager imgr = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btnCancel.setOnClickListener(v -> {
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            requireActivity().getViewModelStore().clear();
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        btnDone.setOnClickListener(v -> {
            save();
            mViewModel.saveSAD();
            requireActivity().getViewModelStore().clear();
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });
    }

    private void save() {
        SupplyAndDisposal sad = mViewModel.mSAD.getValue();
        for (int checked : cgCat.getCheckedChipIds()) {
            sad.mCats.add(SADCategory.valueOf(getView().findViewById(checked).getTag().toString()));
        }
        if (!etName.getText().toString().isEmpty()) {
            sad.setName(etName.getText().toString());
        }
        if (!etWater.getText().toString().isEmpty()) {
            sad.setWater(Double.parseDouble(etWater.getText().toString().replace(",", ".")));
        }
        if (!etPrice.getText().toString().isEmpty()) {
            sad.setPriceNumber(Double.parseDouble(etPrice.getText().toString().replace(",", ".")));
        }
        mViewModel.mSAD.setValue(sad);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", Objects.requireNonNull(etName.getText()).toString());
        outState.putString("water", Objects.requireNonNull(etWater.getText()).toString());
        outState.putString("price", Objects.requireNonNull(etPrice.getText()).toString());
    }

    /**
     * Same as onActivityCreated
     * Set's title to R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.new_sad);
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