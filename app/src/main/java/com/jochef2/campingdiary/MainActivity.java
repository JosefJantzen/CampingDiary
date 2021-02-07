package com.jochef2.campingdiary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = this.getSharedPreferences("DATA", MODE_PRIVATE);
        Log.d("TAG", preferences.getString("CURRENT_REISE", null));
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_drawer);

        NavController navController = Navigation.findNavController(this, R.id.nav_host);


        /*if (preferences.getString("CURRENT_REISE", null) == null && navController.getCurrentDestination().getId() != R.id.allReisenFragment){
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.showReiseFragment, true)
                    .build();
            navController.navigate(R.id.action_showReiseFragment_to_allReisenFragment, savedInstanceState, navOptions);
        }*/


        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.currentReiseFragment, R.id.allReisenFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();

        NavigationUI.setupWithNavController(navigationView, navController);

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.open();
        });

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.cs:

                    return true;
                default:
                    return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.close();
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        });

    }
}