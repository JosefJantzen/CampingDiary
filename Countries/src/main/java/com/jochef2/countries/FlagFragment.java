package com.jochef2.countries;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlagFragment extends Fragment {

    private String mCountryCode = null;

    private ImageView imFlag;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlagFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlagFragment newInstance(String countryCode) {
        FlagFragment fragment = new FlagFragment();
        Bundle args = new Bundle();
        args.putString("countryCode", countryCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCountryCode = getArguments().getString("countryCode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mCountryCode != null) {
            mCountryCode = mCountryCode.toLowerCase();

            Context context = imFlag.getContext();
            int flagImageId = context.getResources().getIdentifier(mCountryCode, "drawable", context.getPackageName());
            imFlag.setImageResource(flagImageId);
        }
    }
}