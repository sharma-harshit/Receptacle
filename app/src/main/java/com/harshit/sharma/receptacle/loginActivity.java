package com.harshit.sharma.receptacle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class loginActivity extends AppCompatActivity
{
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    Button loginButton;
    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        if(isUserLogin())
        {
            loginUser();
            displayMessage("logging in!");
        }
        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                List<AuthUI.IdpConfig> providers = Arrays.asList
                        (
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                        );
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers)
                                .build()
                        ,RC_SIGN_IN
                );
            }
        });

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK)
            {
                loginUser();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                displayMessage("Sign In Failed");
                Log.d("Receptacle " ,data.toString());

            }
            else {
                if (response == null)
                {
                    displayMessage("Sign In Cancelled!!!");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK)
                {
                    displayMessage("No Internet!!!");
                    return;
                }
                displayMessage("Unknown Response");
                Log.e("Receptacle ", "Sign-in error: ", response.getError());
            }
        }

    }

    private void loginUser()
    {
        Intent chatIntent = new Intent(loginActivity.this, user.class);
        startActivity(chatIntent);
        finish();
    }
    private boolean isUserLogin()
    {
        if(mFirebaseAuth.getCurrentUser()!=null)
            return true;
        return  false;
    }
    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    
}
