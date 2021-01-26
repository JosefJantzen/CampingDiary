package com.jochef2.campingdiary;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_drawer);

        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        /*AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
     //                   .setDrawerLayout(drawerLayout)
                        .build();*/

        //NavigationUI.setupWithNavController(navigationView, navController);

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

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            drawerLayout.close();
            return true;
        });

    }
}