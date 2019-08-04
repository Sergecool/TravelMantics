package dev.serge.travelmantics.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.serge.travelmantics.model.TravelDeal;

public class FirebaseUtils {

    private static final int RC_SIGN_IN = 123;
    public static FirebaseDatabase dealDatabase;
    public static DatabaseReference databaseReference;
    public static FirebaseUtils utils;
    public static FirebaseAuth auth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public static ArrayList<TravelDeal> deals;
    private static Activity caller;

    // private constructor to prevent class instanciation
    private FirebaseUtils() { }

    public static void openFirebaseRef(String reference, final Activity activity) {
        if (utils == null) {
            utils = new FirebaseUtils();
            dealDatabase = FirebaseDatabase.getInstance();
            databaseReference = dealDatabase.getReference().child("deals");
            auth = FirebaseAuth.getInstance();
            caller = activity;
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        FirebaseUtils.signIn();
                        Toast.makeText(caller.getBaseContext(), "Welcome back",
                                Toast.LENGTH_LONG).show();
                    }
                }
            };
        }
    }

    private static void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void attachListener() {
        auth.addAuthStateListener(authStateListener);
    }

    public static void detachListener() {
        auth.removeAuthStateListener(authStateListener);
    }

}
