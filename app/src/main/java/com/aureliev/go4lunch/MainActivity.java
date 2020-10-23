package com.aureliev.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //MapsViewFragment opens first when launch
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                new MapsViewFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.map_view_tab:
                            selectedFragment = new MapsViewFragment();
                            break;
                        case R.id.list_view_tab:
                            selectedFragment = new ListViewFragment();
                            break;
                        case R.id.workmates_tab:
                            selectedFragment = new WorkmatesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                            selectedFragment).commit();
                    return true;
                }
            };

}