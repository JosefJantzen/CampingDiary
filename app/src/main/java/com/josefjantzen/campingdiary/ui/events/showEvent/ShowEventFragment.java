package com.josefjantzen.campingdiary.ui.events.showEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.josefjantzen.campingdiary.R;

public class ShowEventFragment extends Fragment {

    private ShowEventViewModel mViewModel;

    public static ShowEventFragment newInstance() {
        return new ShowEventFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_event_fragment, container, false);
    }
}