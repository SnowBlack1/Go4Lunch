package com.aureliev.go4lunch.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aureliev.go4lunch.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private FrameLayout loginActivityLayout;
    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TwitterLoginButton mTwitterLoginButton;

    //Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                        getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);


        setContentView(R.layout.activity_login);
        loginActivityLayout = findViewById(R.id.login_activity_layout);

        mAuth = FirebaseAuth.getInstance();
        TwitterLoginButton mTwitterLoginButton = findViewById(R.id.twitter_login_btn);

        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //Do smth w/ result, which provides a TwitterSession for making API calls
                //mettre toast ?
                signInToFirebaseWithTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                //Do smth on failure
            }
        });


        Button facebookConnectBtn = findViewById(R.id.facebook_login_btn);
        AppEventsLogger.activateApp(getApplication());
        //FacebookSdk.sdkInitialize(getApplicationContext()); //DEPRECATED
        facebookConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button googleConnectBtn = findViewById(R.id.google_login_btn);
        googleConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }


    public void facebookLogin() {
        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList(
                "email", "public_profile"
        ));
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                //Log.d(TAG, "facebook:onError", error);
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            //METTRE INTENT ICI CAR SINON INTENT MEME SI AUTH PAS OK
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        intentMainActivity();
                    }
                });
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void intentMainActivity() {
        Intent MainActivity = new Intent(LoginActivity.this, com.aureliev.go4lunch.activities.MainActivity.class);
        startActivity(MainActivity);
    }

    // Show Snack Bar with a message
    private void showSnackBar(FrameLayout frameLayout, String message) {
        Snackbar.make(frameLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d(TAG, "onActivityResult: " + requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
            }
        } else {
        //Il faut gérer les requestCode qui sont différents de celui de Google
            //Facebook aura toujours un requestcode différent
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

            //Ca ça sert pour le cas de email donc tu n'en a pas besoin ici sauf si tu rajoutes le bouton
            // dans ce cas il faudra que tu crée un nouveau RC_SIGN_IN_EMAIL par exemple pour le
            //différencier de Google
            //            handleResponseAfterSignIn(requestCode, resultCode, data);
        }

    }

    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
//
    //    mCallbackManager.onActivityResult(requestCode, resultCode, data); //PB WHEN GOOGLE LOGIN????
    //    mTwitterLoginButton.onActivityResult(requestCode,resultCode,data);
    //    handleResponseAfterSignIn(requestCode, resultCode, data);
//
    //    //Meme en changeant l183 de place, mCallbackManager == null when login google first
//
    //    //if (mCallbackManager==null){
    //    // mCallbackManager= CallbackManagerImpl@5555;
    //    //}
//
//
//
    //    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    //    if (requestCode == RC_SIGN_IN) {
    //        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
    //        try {
    //            GoogleSignInAccount account = task.getResult(ApiException.class);
    //            firebaseAuthWithGoogle(account.getIdToken());
    //        } catch (ApiException e) {
    //        }
    //    }
//
    //}

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInToFirebaseWithTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, "Signed in firebase twitter successful", Toast.LENGTH_LONG).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Auth firebase twitter failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //EMAIL
    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.loginActivityLayout, getString(R.string.connection_succeed));
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.loginActivityLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.loginActivityLayout, getString(R.string.error_no_internet));
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.loginActivityLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

}