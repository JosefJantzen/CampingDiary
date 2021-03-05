package com.jochef2.campingdiary.ui.allReisen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jochef2.campingdiary.R;

public class AllReisenFragment extends Fragment implements LifecycleObserver {

    public static AllReisenViewModel mViewModel;

    private LinearLayout noReisen;
    private MaterialButton btnStartReise;
    private RecyclerView recyclerView;
    private FloatingActionButton fabStartReise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_reisen_fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        btnStartReise = view.findViewById(R.id.btn_start_reise);
        noReisen = view.findViewById(R.id.no_reise);
        fabStartReise = view.findViewById(R.id.fab_start_reise);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(AllReisenViewModel.class);

        // changes recyclerView on reisen changes
        mViewModel.getAllReisen().observe(getViewLifecycleOwner(), reisen -> {
            ReiseAdapter adapter = new ReiseAdapter(mViewModel.getAllReisen(), getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            if (reisen.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                noReisen.setVisibility(View.VISIBLE);

                btnStartReise.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_allReisenFragment_to_newReisenFragment));
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                noReisen.setVisibility(View.GONE);
            }
        });

        fabStartReise.setOnClickListener(v -> {
            if (mViewModel.getCurrentId().equals("-1")) {
                Navigation.findNavController(getActivity(), R.id.nav_host).navigate(R.id.action_allReisenFragment_to_newReisenFragment);
            } else
                Toast.makeText(getContext(), getString(R.string.err_multiple_reisen), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    /**
     * same as onActivityCreated
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.all_reisen);
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