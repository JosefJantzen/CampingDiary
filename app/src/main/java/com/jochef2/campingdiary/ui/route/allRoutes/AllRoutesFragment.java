package com.jochef2.campingdiary.ui.route.allRoutes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jochef2.campingdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AllRoutesFragment extends Fragment implements LifecycleObserver {

    private AllRoutesViewModel mViewModel;

    private RecyclerView recyclerView;
    private FloatingActionButton fabNewRoute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_routes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        fabNewRoute = view.findViewById(R.id.fab_new_route);

        mViewModel = new ViewModelProvider(requireActivity(), new AllRoutesViewModelFactory(getActivity().getApplication(), AllRoutesFragmentArgs.fromBundle(requireArguments()).getReiseId())).get(AllRoutesViewModel.class);

        mViewModel.mReise.observe(getViewLifecycleOwner(), reise -> {
            AllRoutesAdapter adapter = new AllRoutesAdapter(mViewModel.mReise, getContext(), getChildFragmentManager());
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        });

        fabNewRoute.setOnClickListener(v -> {
            AllRoutesFragmentDirections.ActionAllRoutesFragmentToNewRouteFragment action = AllRoutesFragmentDirections.actionAllRoutesFragmentToNewRouteFragment();
            action.setReiseId(Objects.requireNonNull(mViewModel.mReise.getValue()).mReise.getId());
            Navigation.findNavController(getActivity(), R.id.nav_host).navigate(action);
        });
    }

    /**
     * same as onActivityCreated
     * sets title either to reise.name or R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(getString(R.string.all_routes));
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