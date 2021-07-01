package com.jochef2.campingdiary;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.jochef2.campingdiary.data.values.EventCategory;
import com.jochef2.campingdiary.ui.events.newEvent.NewEventCategorySelectorFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_drawer);

        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.currentReiseFragment, R.id.allReisenFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        // handel drawer selection
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.close();
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (appBarConfiguration.getTopLevelDestinations().contains(destination.getId())) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
    }

    public void onClick(View v) {
        for (EventCategory cat : EventCategory.values()){
            if (cat.name().equals(v.getTag())) {
                NewEventCategorySelectorFragment.onClick(v);
                break;
            }
        }
    }
}