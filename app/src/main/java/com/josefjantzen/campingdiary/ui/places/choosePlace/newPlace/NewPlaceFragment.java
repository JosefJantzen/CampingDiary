package com.josefjantzen.campingdiary.ui.places.choosePlace.newPlace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.ui.maps.placeSelect.MapPlaceSelectDialogFragment;
import com.josefjantzen.campingdiary.ui.places.choosePlace.ChoosePlaceFragment;
import com.josefjantzen.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel;
import com.josefjantzen.campingdiary.ui.places.choosePlace.ChoosePlaceViewModel.FIELDS;
import com.josefjantzen.campingdiary.ui.places.choosePlace.searchPlace.SearchPlaceFragment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NewPlaceFragment extends Fragment {

    private ChoosePlaceViewModel mViewModel;

    public static List<Place.Field> mPlaceFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    private ViewPager2 mPredictionsPager;
    private TabLayout mTabLayout;
    private TextInputEditText etName;
    private TextInputLayout tilName;

    private MaterialCardView cardAutocomplete;
    private MaterialButton btnAutocomplete;
    private TextView txAutocomplete;

    private MaterialCardView cardCords;

    private MaterialCardView cardMap;
    private MaterialButton btnMap;
    private TextView txLatitude;
    private TextView txLongitude;
    private TextView txDistance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.new_place_menu, menu);
        menu.findItem(R.id.it_search).setOnMenuItemClickListener(item -> {
            ChoosePlaceFragment.setFirstPage();
            SearchPlaceFragment.search();
            return true;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_place, container, false);
    }

    private FIELDS lastField = FIELDS.NULL;

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ChoosePlaceViewModel.class);

        mPredictionsPager = view.findViewById(R.id.pager_predictions);
        mTabLayout = view.findViewById(R.id.tabs_prediction);
        txLatitude = view.findViewById(R.id.tx_latitude);
        txLongitude = view.findViewById(R.id.tx_longitude);
        etName = view.findViewById(R.id.et_name);
        tilName = view.findViewById(R.id.til_name);
        btnAutocomplete = view.findViewById(R.id.btn_autocomplete);
        txAutocomplete = view.findViewById(R.id.tx_autocomplete);
        cardAutocomplete = view.findViewById(R.id.card_autocomplete);
        cardCords = view.findViewById(R.id.card_cords);
        cardMap = view.findViewById(R.id.card_map);
        btnMap = view.findViewById(R.id.btn_map);
        txDistance = view.findViewById(R.id.tx_distance);

        // initialize the pager for the bottom bar
        mPredictionsPager.setAdapter(new PredictionsPagerAdapter(requireActivity()));
        new TabLayoutMediator(mTabLayout, mPredictionsPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_gps);
                    tab.setText(R.string.gps);
                    break;
                case 1:
                    tab.setIcon(R.drawable.googleg_disabled_color_18);
                    tab.setText(R.string.google);
                    break;
            }
        }).attach();

        // reset things on devices changes
        if (savedInstanceState != null) {
            etName.setText(savedInstanceState.getString("NAME"));
        }

        // selects the card
        mViewModel.mField.observe(getViewLifecycleOwner(), field -> {
            unselect(field);
            switch (field) {
                case GPS_PREDICTION:
                    //if (Objects.requireNonNull(etName.getText()).toString().isEmpty()){
                    String name = mViewModel.mAddressPredictions.getValue().get(mViewModel.mSelectedGpsPrediction.getValue()).getFeatureName();
                    if (name != null) etName.setText(Objects.requireNonNull(name));
                    //}
                    break;
                case GOOGLE_PREDICTION:
                    //if (Objects.requireNonNull(etName.getText()).toString().isEmpty()){
                    etName.setText(mViewModel.mPlacePredictions.getValue().get(mViewModel.mSelectedGooglePrediction.getValue()).getPlace().getName());
                    //}
                    break;
                case AUTOCOMPLETE:
                    cardAutocomplete.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                    //if (Objects.requireNonNull(etName.getText()).toString().isEmpty()){
                    etName.setText(mViewModel.mSelectedAutocompletePlace.getValue().getName());
                    //}
                    break;
                case CORDS:
                    cardCords.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                    break;
                case MAP:
                    cardMap.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                    etName.setText(mViewModel.mName.getValue());
                    break;
                default:
                    break;
            }
        });

        // saves text of txName in ViewModel
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tilName.setEndIconOnClickListener(v -> {
            etName.setText("");
            mViewModel.mName.setValue("");
        });

        cardAutocomplete.setOnClickListener(v -> {
            // select card and open AutoCompleteDialog if nothing yet selected
            if (txAutocomplete.getText() == getString(R.string.not_selected)) {
                if(!Places.isInitialized()) {
                    //TODO: API-KEY
                    Places.initialize(requireActivity().getApplicationContext(), getString(R.string.MAPS_API_KEY));
                }
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, mPlaceFields)
                        .build(requireContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            } // unselect card
            else if (cardAutocomplete.getCardBackgroundColor().getDefaultColor() == R.attr.colorPrimaryVariant) {
                mViewModel.setField(FIELDS.NULL);
            } // select card
            else {
                cardAutocomplete.setCardBackgroundColor(R.attr.colorPrimaryVariant);
                mViewModel.setField(FIELDS.AUTOCOMPLETE);
            }
        });

        btnAutocomplete.setOnClickListener(v -> {
            if(!Places.isInitialized()) {
                //TODO: API-KEY
                Places.initialize(requireActivity().getApplicationContext(), getString(R.string.MAPS_API_KEY));
            }
            // open AutoCompleteDialog if nothing yet selected
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, mPlaceFields)
                    .build(requireContext());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // observes autocomplete Place to set Name of textView
        mViewModel.getSelectedAutocompletePlace().observe(getViewLifecycleOwner(), place -> txAutocomplete.setText(place.getName()));

        cardCords.setOnClickListener(v -> {
            if (mViewModel.getField() != FIELDS.CORDS) {
                mViewModel.setField(FIELDS.CORDS);
            } else mViewModel.setField(FIELDS.NULL);
        });

        cardMap.setOnClickListener(v -> {
            if (mViewModel.getField() != FIELDS.MAP) {
                mViewModel.setField(FIELDS.MAP);
            } else mViewModel.setField(FIELDS.NULL);
        });

        btnMap.setOnClickListener(v -> {
            String name = mViewModel.getName();
            if (name.equals("")) {
                name = getString(R.string.unnammed);
            }
            MapPlaceSelectDialogFragment.newInstance(getChildFragmentManager(), name, mViewModel.mSelectedMap.getValue().latLng).setInterface((poi) -> {
                mViewModel.mSelectedMap.setValue(poi);
                if (poi.name != null && mViewModel.mName.getValue().equals("")) {
                    mViewModel.mName.setValue(poi.name);
                    etName.setText(poi.name);
                }
                if (mViewModel.getField() != FIELDS.MAP) {
                    mViewModel.setField(FIELDS.MAP);
                }
            });
        });

        // observes selected map place and sets TextViews
        mViewModel.getSelectedMap().observe(getViewLifecycleOwner(), poi -> {
            LatLng latLng = poi.latLng;
            txLatitude.setText(Double.toString(latLng.latitude));
            txLongitude.setText(Double.toString(latLng.longitude));

            Location location = new Location("");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            double distance = mViewModel.mCurrentLocation.getValue().distanceTo(location);
            if (distance >= 1000) {
                txDistance.setText(new BigDecimal(distance / 1000).setScale(1, BigDecimal.ROUND_HALF_EVEN) + " km");
            } else {
                txDistance.setText(new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_EVEN) + " m");
            }
        });
    }

    /**
     * unselects card of @param field
     *
     * @param field name of field that should unselect
     */
    private void unselect(FIELDS field) {
        MaterialCardView cardView = new MaterialCardView(requireContext());
        int defaultColor = cardView.getCardBackgroundColor().getDefaultColor();
        switch (lastField) {
            case GPS_PREDICTION:
                GpsPredictionFragment.unselect();
                break;
            case GOOGLE_PREDICTION:
                GooglePredictionFragment.unselect();
                break;
            case AUTOCOMPLETE:
                cardAutocomplete.setCardBackgroundColor(defaultColor);
                break;
            case CORDS:
                cardCords.setCardBackgroundColor(defaultColor);
                break;
            case MAP:
                cardMap.setCardBackgroundColor(defaultColor);
                break;
            default:
                break;
        }
        lastField = field;
    }

    /**
     * called then AutocompleteSupportFragment has a result
     *
     * @param requestCode code to identify request
     * @param resultCode  status of request
     * @param data        data from the result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));
                mViewModel.setSelectedAutocompletePlace(place);
                mViewModel.setField(FIELDS.AUTOCOMPLETE);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Log.e("TAG", "Autocomplete Error: " + Autocomplete.getStatusFromIntent(Objects.requireNonNull(data)).getStatusMessage());
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                //do something when cancel
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * called after devices changes
     *
     * @param outState bundle of saved params
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NAME", Objects.requireNonNull(etName.getText()).toString());
    }

    /**
     * Adpater for viewPager of Perdictions TabLayout
     */
    private static class PredictionsPagerAdapter extends FragmentStateAdapter {
        public PredictionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @SuppressWarnings("ConstantConditions")
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new GpsPredictionFragment();
                case 1:
                    return new GooglePredictionFragment();
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