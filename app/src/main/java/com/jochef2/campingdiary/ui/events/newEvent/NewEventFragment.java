package com.jochef2.campingdiary.ui.events.newEvent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.jochef2.campingdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NewEventFragment extends Fragment implements LifecycleObserver {

    public static NewEventViewModel mViewModel;
    private static ViewPager2 mViewPager;
    private static FragmentActivity mActivity;

    /**
     * sets Tab of ViewPager
     *
     * @param position of tab
     */
    public static void setTab(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    /**
     * pops BackStack to navigate back
     */
    public static void navigateBack() {
        mActivity.getViewModelStore().clear();
        Navigation.findNavController(mActivity, R.id.nav_host).popBackStack();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_event_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(new NewEventFragment.ScreenSlidePagerAdapter(requireActivity()));
        mViewPager.setUserInputEnabled(false);

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewEventViewModel.class);
        mViewModel.setReiseId(NewEventFragmentArgs.fromBundle(requireArguments()).getReiseId());

        mActivity = requireActivity();
    }

    /**
     * Same as onActivityCreated
     * Set's title to R.string.start_tour
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreated() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.new_event);
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

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new NewEventCategorySelectorFragment();
                case 1:
                    return new NewEventSecondFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}