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
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.NavMainDirections;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Night;
import com.jochef2.campingdiary.data.values.Events;

import java.util.Calendar;
import java.util.Objects;

public class NewNightFirstFragment extends Fragment {

    private TextInputEditText etName;
    private ChipGroup chEnd;
    private MaterialButton btnClose;
    private MaterialButton btnNext;
    private TextView txEnd;
    private Chip chEndCustom;
    private ChipGroup chStart;
    private Chip chStartCustom;
    private TextView txStart;
    private MaterialButton btnChoosePlace;
    private TextView txPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_night_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        chEnd = view.findViewById(R.id.ch_end);
        btnClose = view.findViewById(R.id.btn_back);
        btnNext = view.findViewById(R.id.btn_check);
        txEnd = view.findViewById(R.id.tx_end);
        chEndCustom = view.findViewById(R.id.ch_end_custom);
        chStart = view.findViewById(R.id.ch_start);
        chStartCustom = view.findViewById(R.id.ch_start_custom);
        txStart = view.findViewById(R.id.tx_start);
        btnChoosePlace = view.findViewById(R.id.btn_choose_place);
        txPlace = view.findViewById(R.id.tx_place);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
        }

        NewNightFragment.mViewModel.mNight.observe(getViewLifecycleOwner(), night -> {
            txEnd.setText(night.getEndDate());
        });

        NewNightFragment.mViewModel.mPlace.observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                txPlace.setText(place.mPlace.getPlaceName());
                if (etName.getText().toString().isEmpty()) {
                    etName.setText(place.mPlace.getPlaceName());
                }
            }
        });

        btnChoosePlace.setOnClickListener(v -> {
            NavMainDirections.ActionGlobalChoosePlaceFragment action = NavMainDirections.actionGlobalChoosePlaceFragment();
            action.setCat(Events.NIGHT);
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(action);
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

        chStart.setOnCheckedChangeListener(((group, checkedId) -> {
            Night night = NewNightFragment.mViewModel.mNight.getValue();
            if (checkedId != R.id.ch_start_custom) {
                Objects.requireNonNull(night).setBegin(Calendar.getInstance());

                if (chEnd.getCheckedChipId() == R.id.ch_one || chEnd.getCheckedChipId() == R.id.ch_two) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(view.findViewById(chEnd.getCheckedChipId()).getTag().toString()));
                    night.setEnd(c);
                }
                NewNightFragment.mViewModel.mNight.setValue(night);
                NewNightFragment.mViewModel.lastStartChip = checkedId;
                txStart.setText(getString(R.string.today));
            }
        }));

        chEnd.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId != R.id.ch_end_custom) {
                Night night = NewNightFragment.mViewModel.mNight.getValue();
                Calendar c = night.getBegin();
                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(view.findViewById(checkedId).getTag().toString()));
                night.setEnd(c);
                NewNightFragment.mViewModel.mNight.setValue(night);
                NewNightFragment.mViewModel.lastEndChip = checkedId;
            }
        });

        chStartCustom.setOnClickListener(v -> {
            Night night = NewNightFragment.mViewModel.mNight.getValue();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                Calendar begin = Calendar.getInstance();
                begin.set(year, month, dayOfMonth);
                Objects.requireNonNull(night).setBegin(begin);

                if (begin.after(night.getEnd())) {
                    Calendar calendar = night.getBegin();
                    calendar.setTimeInMillis(night.getBegin().getTimeInMillis());
                    int addDays = 1;
                    if (chEnd.getCheckedChipId() == R.id.ch_two) {
                        addDays = 2;
                    } else if (chEnd.getCheckedChipId() == R.id.ch_end_custom) {
                        chEnd.check(R.id.ch_one);
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, addDays);
                    night.setEnd(calendar);
                }
                if (chEnd.getCheckedChipId() == R.id.ch_one && begin.get(Calendar.DAY_OF_YEAR) != (night.getEnd().get(Calendar.DAY_OF_YEAR) - 1)) { // !!! Neujahr funktioniert nicht
                    Calendar calendar = night.getBegin();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    night.setEnd(calendar);
                } else if (chEnd.getCheckedChipId() == R.id.ch_two && begin.get(Calendar.DAY_OF_YEAR) != (night.getEnd().get(Calendar.DAY_OF_YEAR) - 2)) {
                    Calendar calendar = night.getBegin();
                    calendar.add(Calendar.DAY_OF_MONTH, 2);
                    night.setEnd(calendar);
                }

                txStart.setText(night.getBeginDate());
                NewNightFragment.mViewModel.mNight.setValue(night);
                NewNightFragment.mViewModel.lastStartChip = R.id.ch_start_custom;

            }, Objects.requireNonNull(night).getBegin().get(Calendar.YEAR), night.getBegin().get(Calendar.MONTH), night.getBegin().get(Calendar.DAY_OF_MONTH));

            datePickerDialog.setOnCancelListener(dialog -> chStart.check(NewNightFragment.mViewModel.lastStartChip));
            datePickerDialog.show();
        });

        chEndCustom.setOnClickListener(v -> {
            Night night = NewNightFragment.mViewModel.mNight.getValue();
            Calendar c = Objects.requireNonNull(night).getEnd();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                Calendar end = Calendar.getInstance();
                end.set(year, month, dayOfMonth);
                night.setEnd(end);
                NewNightFragment.mViewModel.mNight.setValue(night);
                NewNightFragment.mViewModel.lastEndChip = R.id.ch_end_custom;
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(night.getBegin().getTimeInMillis() + 24 * 60 * 60 * 1000);
            datePickerDialog.setOnCancelListener(dialog -> chEnd.check(NewNightFragment.mViewModel.lastEndChip));
            datePickerDialog.show();
        });
    }

    private void save() {
        Night night = NewNightFragment.mViewModel.mNight.getValue();
        night.setName(etName.getText().toString());
        NewNightFragment.mViewModel.mNight.setValue(night);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", Objects.requireNonNull(etName.getText()).toString());
    }
}