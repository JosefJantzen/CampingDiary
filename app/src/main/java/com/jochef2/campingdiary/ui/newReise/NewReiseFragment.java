package com.jochef2.campingdiary.ui.newReise;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Reise;

import java.util.Calendar;

public class NewReiseFragment extends Fragment implements LifecycleObserver {

    private NewReiseViewModel mViewModel;

    private TextInputEditText etName;
    private Button btnClose;
    private Button btnCheck;
    private ChipGroup chEnd;
    private TextView txEnd;
    private Chip chCustom;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reise_fragment, container, false);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        etName = view.findViewById(R.id.et_name);
        btnClose = view.findViewById(R.id.btn_back);
        btnCheck = view.findViewById(R.id.btn_check);
        chEnd = view.findViewById(R.id.end_group);
        txEnd = view.findViewById(R.id.tx_end);
        chCustom = view.findViewById(R.id.ch_custom);

        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewReiseViewModel.class);

        // save current name e.g. on rotation
        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
        }

        // set date in TextView
        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            txEnd.setText(reise.getEndDate());
        });

        // handle date changes
        chEnd.setOnCheckedChangeListener((group, checkedId) -> {
            Reise reise = mViewModel.mReise.getValue();
            // if preconfigured weeks
            if (checkedId != R.id.ch_custom) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_WEEK, Integer.parseInt((String) view.findViewById(checkedId).getTag()));
                reise.setEnd(c);
                mViewModel.mReise.setValue(reise);
                mViewModel.lastChip = checkedId;
            }
        });

        chCustom.setOnClickListener(v -> {
            Reise reise = mViewModel.mReise.getValue();
            Calendar c = reise.getEnd();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                Calendar end = Calendar.getInstance();
                end.set(year, month, dayOfMonth);
                reise.setEnd(end);
                mViewModel.mReise.setValue(reise);
                mViewModel.lastChip = R.id.ch_custom;
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + 24 * 60 * 60 * 1000);
            datePickerDialog.setOnCancelListener(dialog -> chEnd.check(mViewModel.lastChip));
            datePickerDialog.show();
        });

        btnClose.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_newReisenFragment_to_allReisenFragment));

        btnCheck.setOnClickListener(v -> {
            Reise reise = mViewModel.mReise.getValue();
            reise.setName(etName.getText().toString());

            if (!reise.getName().equals("")) {
                mViewModel.mReise.setValue(reise);
                mViewModel.saveReise();

                // remove newReiseFragment from BackStack
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.newReisenFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_newReisenFragment_to_currentReiseFragment, savedInstanceState, navOptions);
            } else {
                Toast.makeText(getContext(), getString(R.string.err_no_name), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Same as onActivityCreated
     * Set's title to R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.start_reise);
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

    /**
     * save name on e.g. screen rotation
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", etName.getText().toString());
    }
}