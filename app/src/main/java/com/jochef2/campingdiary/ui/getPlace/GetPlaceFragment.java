package com.jochef2.campingdiary.ui.getPlace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.newNight.NewNightViewModel;

public class GetPlaceFragment extends Fragment {

    private GetPlaceViewModel mViewModel;

    public static GetPlaceFragment newInstance() {
        return new GetPlaceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.get_place_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        NewNightViewModel viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewNightViewModel.class);
    }
}