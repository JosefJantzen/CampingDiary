package com.josefjantzen.campingdiary.ui.events.newEvent;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.entities.Event;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;


public class NewEventSecondFragment extends Fragment {

    private TextView txStart;
    private ChipGroup cgStartDay;
    private Chip chStartDayCustom;
    private ChipGroup cgStartTime;
    private Chip chStartTimeCustom;
    private TextView txEnd;
    private ChipGroup cgEndDay;
    private Chip chEndDayCustom;
    private ChipGroup cgEndTime;
    private Chip chEndTimeCustom;
    private MaterialButton btnBack;
    private MaterialButton btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event_second, container, false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txStart = view.findViewById(R.id.tx_start);
        cgStartDay = view.findViewById(R.id.cg_start_day);
        chStartDayCustom = view.findViewById(R.id.ch_custom_start_day);
        cgStartTime = view.findViewById(R.id.cg_start_time);
        chStartTimeCustom = view.findViewById(R.id.ch_custom_start_time);
        txEnd = view.findViewById(R.id.tx_end);
        cgEndDay = view.findViewById(R.id.cg_end_day);
        chEndDayCustom = view.findViewById(R.id.ch_end_custom_day);
        cgEndTime = view.findViewById(R.id.cg_end_time);
        chEndTimeCustom = view.findViewById(R.id.ch_end_custom_time);
        btnBack = view.findViewById(R.id.btn_back);
        btnNext = view.findViewById(R.id.btn_done);

        btnBack.setOnClickListener(v -> {
            NewEventFragment.setTab(0);
        });

        btnNext.setOnClickListener(v -> {
            NewEventFragment.setTab(2);
        });

        cgStartDay.setOnCheckedChangeListener((group, checkedId) -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            if (checkedId == R.id.ch_start_today) {
                if (cgStartTime.getCheckedChipId() == R.id.ch_custom_start_time) {
                    Calendar c = Calendar.getInstance();
                    event.mBegin.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    txStart.setText(event.getBeginTime());
                    NewEventFragment.mViewModel.mEvent.setValue(event);
                    NewEventFragment.mViewModel.lastStartDayChip = checkedId;
                }
            }
        });

        chStartDayCustom.setOnClickListener(v -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), (view12, year, month, dayOfMonth) -> {
                Calendar c = (Calendar) event.getBegin().clone();
                long time = event.getEnd().getTimeInMillis() - c.getTimeInMillis();
                c.set(year, month, dayOfMonth);

                event.mBegin = (Calendar) c.clone();
                txStart.setText(event.getBeginDateTime());
                cgStartTime.check(R.id.ch_custom_start_time);
                NewEventFragment.mViewModel.lastStartTimeChip = R.id.ch_custom_start_time;
                NewEventFragment.mViewModel.lastStartDayChip = R.id.ch_custom_start_day;

                if (c.after(event.getEnd())) {
                    c.setTimeInMillis(event.getBegin().getTimeInMillis() + time);
                    event.mEnd = c;

                    txEnd.setText(event.getEndDateTime());
                    cgEndDay.check(R.id.ch_end_custom_day);
                    NewEventFragment.mViewModel.lastEndDayChip = R.id.ch_end_custom_day;
                    cgEndTime.check(R.id.ch_end_custom_time);
                    NewEventFragment.mViewModel.lastEndTimeChip = R.id.ch_end_custom_time;
                }

                cgStartDay.check(R.id.ch_custom_start_day);
                NewEventFragment.mViewModel.lastStartDayChip = R.id.ch_custom_start_day;
                NewEventFragment.mViewModel.mEvent.setValue(event);

            }, event.getBegin().get(Calendar.YEAR), event.getBegin().get(Calendar.MONTH), event.getBegin().get(Calendar.DAY_OF_MONTH));

            datePickerDialog.setOnCancelListener(dialog -> {
                cgStartDay.check(NewEventFragment.mViewModel.lastStartDayChip);
            });
            datePickerDialog.show();
        });

        cgStartTime.setOnCheckedChangeListener((group, checkedId) -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            switch (checkedId) {
                case R.id.ch_start_now:
                    event.setBegin(Calendar.getInstance());
                    txStart.setText(getString(R.string.now));
                    if (NewEventFragment.mViewModel.lastStartDayChip == R.id.ch_custom_start_day) {
                        cgStartDay.check(R.id.ch_start_today);
                        NewEventFragment.mViewModel.lastStartDayChip = R.id.ch_start_today;
                    }
                    NewEventFragment.mViewModel.mEvent.setValue(event);
                    NewEventFragment.mViewModel.lastStartTimeChip = checkedId;
                    break;
                case R.id.ch_start_one:
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.HOUR_OF_DAY, 1);
                    event.setBegin(c);
                    txStart.setText(R.string.in_one_hour);
                    if (NewEventFragment.mViewModel.lastStartDayChip == R.id.ch_custom_start_day) {
                        cgStartDay.check(R.id.ch_start_today);
                    }
                    if (cgEndTime.getCheckedChipId() == R.id.ch_end_one) {
                        cgEndTime.check(R.id.ch_end_two);
                        NewEventFragment.mViewModel.lastEndTimeChip = R.id.ch_end_two;
                    }
                    NewEventFragment.mViewModel.mEvent.setValue(event);
                    NewEventFragment.mViewModel.lastStartTimeChip = checkedId;
                    break;
            }
        });

        chStartTimeCustom.setOnClickListener(v -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), (view1, hourOfDay, minute) -> {
                Calendar c = (Calendar) event.mBegin.clone();
                long time = event.getEnd().getTimeInMillis() - c.getTimeInMillis();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                event.mBegin = (Calendar) c.clone();
                if (cgStartDay.getCheckedChipId() == R.id.ch_start_today) {
                    txStart.setText(event.getBeginTime());
                }
                else {
                    txStart.setText(event.getBeginDateTime());
                }
                cgStartTime.check(R.id.ch_custom_start_time);
                NewEventFragment.mViewModel.lastStartTimeChip = R.id.ch_custom_start_time;

                if (c.after(event.getEnd())) {
                    c.setTimeInMillis(event.getBegin().getTimeInMillis() + time);
                    event.mEnd = c;

                    txEnd.setText(event.getEndDateTime());
                    cgEndDay.check(R.id.ch_end_custom_day);
                    NewEventFragment.mViewModel.lastEndDayChip = R.id.ch_end_custom_day;
                    cgEndTime.check(R.id.ch_end_custom_time);
                    NewEventFragment.mViewModel.lastEndTimeChip = R.id.ch_end_custom_time;
                }
                NewEventFragment.mViewModel.mEvent.setValue(event);

            }, event.getBegin().get(Calendar.HOUR_OF_DAY), event.getBegin().get(Calendar.MINUTE), true);

            timePickerDialog.setOnCancelListener(dialog -> {
                cgStartTime.check(NewEventFragment.mViewModel.lastStartTimeChip);
            });
            timePickerDialog.show();
        });

        cgEndDay.setOnCheckedChangeListener((group, checkedId) -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            if (checkedId == R.id.ch_end_today && cgEndTime.getCheckedChipId() == R.id.ch_end_custom_time) {
                Calendar c = Calendar.getInstance();
                event.mEnd.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                txEnd.setText(event.getEndTime());
                NewEventFragment.mViewModel.mEvent.setValue(event);
                NewEventFragment.mViewModel.lastEndDayChip = R.id.ch_end_today;
            }
        });

        chEndDayCustom.setOnClickListener(v -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), (view1, year, month, dayOfMonth) -> {
                Calendar c = (Calendar) event.getEnd().clone();
                c.set(year, month, dayOfMonth);

                if (c.after(event.getBegin())) {
                    event.mEnd = (Calendar) c.clone();
                    txEnd.setText(event.getEndDateTime());
                    cgEndTime.check(R.id.ch_end_custom_time);
                    cgEndDay.check(R.id.ch_end_custom_day);
                    NewEventFragment.mViewModel.lastEndTimeChip = R.id.ch_end_custom_time;
                    NewEventFragment.mViewModel.lastEndDayChip = R.id.ch_end_custom_day;
                    NewEventFragment.mViewModel.mEvent.setValue(event);
                }
                else {
                    Toast.makeText(requireActivity(), getString(R.string.toast_end_day_before_start), Toast.LENGTH_SHORT).show();
                    cgEndDay.check(NewEventFragment.mViewModel.lastEndDayChip);
                }
            }, event.getEnd().get(Calendar.YEAR), event.getEnd().get(Calendar.MONTH), event.getEnd().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setOnCancelListener(dialog -> {
                cgEndDay.check(NewEventFragment.mViewModel.lastEndDayChip);
            });
            datePickerDialog.show();
        });

        cgEndTime.setOnCheckedChangeListener((group, checkedId) -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            Calendar c = Calendar.getInstance();
            switch (checkedId) {
                case R.id.ch_end_one:
                    if (cgStartTime.getCheckedChipId() == R.id.ch_start_one) {
                        Toast.makeText(requireActivity(), getString(R.string.toast_start_and_end_one), Toast.LENGTH_SHORT).show();
                        break;
                    }else {
                        c.add(Calendar.HOUR_OF_DAY, 1);
                        event.setEnd(c);
                        txEnd.setText(getString(R.string.in_one_hour));
                    }
                    if (NewEventFragment.mViewModel.lastEndDayChip == R.id.ch_end_custom_day) {
                        cgEndDay.check(R.id.ch_end_today);
                    }
                    NewEventFragment.mViewModel.mEvent.setValue(event);
                    NewEventFragment.mViewModel.lastEndTimeChip = checkedId;
                    break;
                case R.id.ch_end_two:
                    c.add(Calendar.HOUR_OF_DAY, 2);
                    event.setEnd(c);
                    txEnd.setText(getString(R.string.in_two_hours));
                    if (NewEventFragment.mViewModel.lastEndDayChip == R.id.ch_end_custom_day) {
                        cgEndDay.check(R.id.ch_end_today);
                    }
                    NewEventFragment.mViewModel.mEvent.setValue(event);
                    NewEventFragment.mViewModel.lastEndTimeChip = checkedId;
                    break;
            }
        });

        chEndTimeCustom.setOnClickListener(v -> {
            Event event = NewEventFragment.mViewModel.mEvent.getValue();
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), (view1, hourOfDay, minute) -> {
                Calendar c = (Calendar) event.getEnd().clone();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                if (c.after(event.getBegin())){
                    event.setEnd(c);
                    if (cgEndDay.getCheckedChipId() == R.id.ch_end_today) {
                        txEnd.setText(event.getEndTime());
                    }
                    else {
                        txEnd.setText(event.getEndDateTime());
                    }
                    cgEndTime.check(R.id.ch_end_custom_time);
                    NewEventFragment.mViewModel.lastEndTimeChip = R.id.ch_end_custom_time;
                }
                else {
                    Toast.makeText(requireActivity(), getString(R.string.toast_end_time_before_start), Toast.LENGTH_SHORT).show();
                    cgEndTime.check(NewEventFragment.mViewModel.lastEndTimeChip);
                }

            }, event.getEnd().get(Calendar.HOUR_OF_DAY), event.getEnd().get(Calendar.MINUTE), true);
            timePickerDialog.setOnCancelListener(dialog -> {
                cgEndTime.check(NewEventFragment.mViewModel.lastEndTimeChip);
            });
            timePickerDialog.show();
        });
    }
}