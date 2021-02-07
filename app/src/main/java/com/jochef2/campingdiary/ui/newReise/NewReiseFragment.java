package com.jochef2.campingdiary.ui.newReise;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.google.android.material.textfield.TextInputEditText;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.data.entities.Reise;

public class NewReiseFragment extends Fragment implements LifecycleObserver {

    private NewReiseViewModel mViewModel;

    private TextInputEditText etName;
    private Button btnClose;
    private Button btnCheck;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reise_fragment, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        btnClose = view.findViewById(R.id.btn_close);
        btnCheck = view.findViewById(R.id.btn_check);

        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(NewReiseViewModel.class);

        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("name"));
        }

        btnClose.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_newReisenFragment_to_allReisenFragment);
        });

        btnCheck.setOnClickListener(v -> {
            Reise reise = mViewModel.mReise.getValue();
            reise.setName(etName.getText().toString());

            if (!reise.getName().equals("")) {
                mViewModel.mReise.postValue(reise);
                mViewModel.saveReise();

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.newReisenFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_newReisenFragment_to_currentReiseFragment, savedInstanceState, navOptions);
            } else {
                Toast.makeText(getContext(), getString(R.string.err_no_name), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.start_reise);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getLifecycle().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getLifecycle().removeObserver(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", etName.getText().toString());
    }
}