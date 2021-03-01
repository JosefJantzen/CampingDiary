package com.jochef2.campingdiary.ui.newNight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.jochef2.campingdiary.R;

public class NewNightFragment extends Fragment {

    public static NewNightViewModel mViewModel;
    private static ViewPager2 viewPager;
    private static Bundle saved;
    private static FragmentActivity activity;

    public static void setTab(int postion) {
        viewPager.setCurrentItem(postion, true);
    }

    public static void navigateBack() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.newNightFragment, true)
                .build();
        Navigation.findNavController(activity, R.id.nav_host).navigate(R.id.action_newNightFragment_to_currentReiseFragment, saved, navOptions);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_night_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(requireActivity()));
        viewPager.setUserInputEnabled(false);

        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NewNightViewModel.class);
        mViewModel.setReiseId(NewNightFragmentArgs.fromBundle(requireArguments()).getReiseId());

        saved = savedInstanceState;
        activity = requireActivity();
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
                    return new NewNightFirstFragment();
                case 1:
                    return new NewNightSecondFragment();
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