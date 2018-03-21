package com.qtechie.gmailloginapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    //Google sign in api Client
    GoogleSignInClient mGoogleSignInClient;

    //Define Request code for Sign In
    private int RC_SIGN_IN = 6;

    TextView textViewEmail;
    TextView textViewPersonName;
    ImageView imageViewProfilePic;
    LinearLayout llProfileLayout;

    //Logout Button declaration
    Button buttonLogout;

    //Sign in button Declaration
    SignInButton signInButton;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Bind views
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPersonName = view.findViewById(R.id.textViewPersonName);
        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic);
        llProfileLayout = view.findViewById(R.id.llProfileLayout);
        signInButton = view.findViewById(R.id.sign_in_button);
        buttonLogout = view.findViewById(R.id.buttonLogout);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        //get Sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        //get currently signed in user returns null if there is no logged in user
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        //update ui
        updateUI(account);

    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // [END onActivityResult]
    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            if (e.getStatusCode() == CommonStatusCodes.NETWORK_ERROR)
                Log.w(TAG, "signInResult:failed. Couldn't connect to Google" + e.getStatusCode());
            else
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    //Method to signIn
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //method to sign out
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUI(null);
                        }
                    }
                });
    }

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        //Account is not null then user is logged in
        if (account != null) {
            signInButton.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.VISIBLE);
            textViewEmail.setText(account.getEmail());
            textViewPersonName.setText(account.getDisplayName());
            Picasso.with(getContext()).load(account.getPhotoUrl()).fit().into(imageViewProfilePic);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            //user is not logged in
            // Set the dimensions of the sign-in button.
            signInButton.setSize(SignInButton.SIZE_WIDE);
            signInButton.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }
}
