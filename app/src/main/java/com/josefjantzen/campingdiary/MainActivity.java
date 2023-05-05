package com.josefjantzen.campingdiary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.josefjantzen.campingdiary.ui.reisen.currentReise.CurrentReiseFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Integer> mCurrents = Arrays.asList(R.id.allNightsFragment, R.id.allEventsFragment, R.id.allFuelsFragment, R.id.allSADsFragment);

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
                new AppBarConfiguration.Builder(R.id.currentReiseFragment, R.id.allReisenFragment, R.id.allNightsFragment, R.id.allEventsFragment, R.id.allFuelsFragment, R.id.allSADsFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        // handel drawer selection
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.close();
            if(mCurrents.contains(item.getItemId())) {
                Bundle bundle = new Bundle();
                bundle.putInt("reiseId", CurrentReiseFragment.mReiseId);
                navController.popBackStack(R.id.currentReiseFragment, false);
                navController.navigate(item.getItemId(), bundle);
                return true;
            }
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
}