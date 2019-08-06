package dev.serge.travelmantics.utils;
/**
 * Utility class to perform firebase operations
 */


import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.serge.travelmantics.model.TravelDeal;
import dev.serge.travelmantics.ui.DealsListActivity;

public class FirebaseUtils {

    private static final int RC_SIGN_IN = 1000;
    public static FirebaseDatabase dealDatabase;
    public static DatabaseReference databaseReference;
    private static FirebaseUtils utils;
    public static FirebaseAuth auth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public static ArrayList<TravelDeal> deals;
    private static DealsListActivity caller;
    public static boolean isAdmin;
    // private constructor to prevent class instanciation
    private FirebaseUtils() { }

    public static void openFirebaseRef(String reference, final DealsListActivity activity) {
        if (utils == null) {
            utils = new FirebaseUtils();
            dealDatabase = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();
            caller = activity;

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        FirebaseUtils.signIn();
                    }
                    else {
                        String userId = firebaseAuth.getUid();
                        checkIsAdmin(userId);
                    }
                    Toast.makeText(activity.getBaseContext(), "Welcome back!", Toast.LENGTH_LONG).show();
                }
            };
            // connectStorage();

        }

        deals = new ArrayList<>();
        databaseReference = dealDatabase.getReference().child(reference);
    }

    private static void checkIsAdmin(String userId) {
        FirebaseUtils.isAdmin = false;
        DatabaseReference reference = dealDatabase.getReference().child("admin").child(userId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtils.isAdmin = true;
                caller.fabVisibility();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addChildEventListener(listener);
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
                        .setIsSmartLockEnabled(false)
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
