package com.qtechie.gmailloginapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BlankFragment blankFragment = new BlankFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frm, blankFragment, "TAG");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
/*GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                .build();
        GoogleApiClient googleApiClient=new GoogleApiClient.Builder(this)
                                        .enableAutoManage(this,this)
                                        .addApi(Auth.G)*/