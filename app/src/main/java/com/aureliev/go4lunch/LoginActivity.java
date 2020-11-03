package com.aureliev.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    FrameLayout loginActivityLayout;
    //FOR DATA
    //Identifier for Sign-In Activity (????????????????????????????????????????)
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivityLayout = findViewById(R.id.login_activity_layout);

        Button facebookConnectBtn = findViewById(R.id.facebook_connect_btn);
        facebookConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignInActivityFacebook();

                //Intent MainActivity = new Intent(LoginActivity.this, com.aureliev.go4lunch.MainActivity.class);
                //startActivity(MainActivity);

            }
        });

        Button googleConnectBtn = findViewById(R.id.google_connect_btn);
        googleConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignInActivityGoogle();
            }
        });


    }

    public void startSignInActivityGoogle() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(), //GOOGLE
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())) //FACEBOOK
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);

        intentMainActivity();
    }

    private void startSignInActivityFacebook() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(), //GOOGLE
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())) // FACEBOOK
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);

        intentMainActivity();
    }

    public void intentMainActivity() {
        Intent MainActivity = new Intent(LoginActivity.this, com.aureliev.go4lunch.MainActivity.class);
        startActivity(MainActivity);
    }

    //Show Snack Bar with a message
    //private void showSnackBar(FrameLayout frameLayout, String message){
    //Snackbar.make(frameLayout, message, Snackbar.LENGTH_SHORT).show();
    //}

    // Method that handles response after SignIn Activity close
    //private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

    //    IdpResponse response = IdpResponse.fromResultIntent(data);

    //    if (requestCode == RC_SIGN_IN) {
    //        if (resultCode == RESULT_OK) { // SUCCESS
    //            showSnackBar(this.loginActivityLayout, getString(R.string.connection_succeed));
    //        } else { // ERRORS
    //            if (response == null) {
    //                showSnackBar(this.loginActivityLayout, getString(R.string.error_authentication_canceled));
    //            } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
    //                showSnackBar(this.loginActivityLayout, getString(R.string.error_no_internet));
    //            } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
    //                showSnackBar(this.loginActivityLayout, getString(R.string.error_unknown_error));
    //            }
    //        }
    //    }
    //}
}