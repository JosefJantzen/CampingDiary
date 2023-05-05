package com.josefjantzen.campingdiary.ui.route.newRoute;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.josefjantzen.campingdiary.data.entities.Route;
import com.josefjantzen.campingdiary.data.repositories.ReisenRepository;
import com.josefjantzen.campingdiary.ui.reisen.currentReise.CurrentReiseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class NewRouteViewModel extends AndroidViewModel {

    private final ReisenRepository mReisenRepository;
    public MutableLiveData<Route> mRoute = new MutableLiveData<>();
    public int reiseId;

    public NewRouteViewModel(@NonNull @NotNull Application application) {
        super(application);

        mReisenRepository = CurrentReiseViewModel.mReisenRepository;

        Route route = new Route(-1, "", Calendar.getInstance());
        mRoute.setValue(route);
    }

    public void setReiseId(int reiseId) {
        Route route = mRoute.getValue();
        Objects.requireNonNull(route).setReiseId(reiseId);
        mRoute.setValue(route);
    }

    public void saveRoute() {
        mReisenRepository.insertRoute(mRoute.getValue());
    }
}