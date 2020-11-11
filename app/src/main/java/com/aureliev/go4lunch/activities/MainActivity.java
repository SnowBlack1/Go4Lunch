package com.aureliev.go4lunch.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aureliev.go4lunch.R;
import com.aureliev.go4lunch.ViewModel;
import com.aureliev.go4lunch.fragments.ListViewFragment;
import com.aureliev.go4lunch.fragments.MapsViewFragment;
import com.aureliev.go4lunch.fragments.WorkmatesFragment;
import com.aureliev.go4lunch.model.User;
//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private User currentUser;
    private ViewModel mViewModel;

    //Identify each Http Request
    private static final int SIGN_OUT_TASK = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //MapsViewFragment opens first when launch
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                new MapsViewFragment()).commit();

        configureToolbar();
        configureDrawerLayout();
        configureNavigationView();

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Log.e("","TEST POUR SAVOIR");
        //if (user != null){
        //    String userName = user.getDisplayName();
        //    TextView headerName = findViewById(R.id.nav_header_name_txt);
        //    headerName.setText(userName);
        //}

    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void configureToolbar() {
        this.mToolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mToolbar);
    }

    private void configureDrawerLayout() {
        this.mDrawerLayout = findViewById(R.id.main_activity_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        this.mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

   //private void getCurrentUser() {
   //    String uidUser = FirebaseAuth.getInstance().getUid();
   //    this.mViewModel.getUserCurrentMutableLiveData(uidUser).observe(this, user -> {
   //        updateNavigationHeader(user);
   //        currentUser = user;
   //    });
   //}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_drawer_lunch:
                break;
            case R.id.nav_drawer_settings:
                break;
            case R.id.nav_drawer_logout:
                signOutUserFromFirebase();
                break;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void signOutUserFromFirebase() {
        AuthUI.getInstance().signOut(this);
                //.addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));

        backToLoginActivityWhenLogout();

    }

    private void backToLoginActivityWhenLogout() {
        Intent LoginActivity = new Intent(MainActivity.this, com.aureliev.go4lunch.activities.LoginActivity.class);
        startActivity(LoginActivity);
    }

    private void updateNavigationHeader(User currentUser) {
        final View headerView = mNavigationView.getHeaderView(0);
        TextView nameUser = headerView.findViewById(R.id.nav_header_name_txt);
        TextView emailUser = headerView.findViewById(R.id.nav_header_email_txt);
        ImageView illustrationUser = headerView.findViewById(R.id.nav_header_image_view);
        nameUser.setText(currentUser.getName());
        emailUser.setText(currentUser.getEmail());
        if (currentUser.getProfilePicture() != null)
        {
            Glide.with(this).load(currentUser.getProfilePicture()).circleCrop().into(illustrationUser);
        }
    }


    //Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }
}


