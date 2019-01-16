package com.app.geoloc.geo1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import geo11.app.R;

public class Login extends AppCompatActivity {

    private static final int ANON_MODE = 100;
    private static final int CREATE_MODE = 101;
    private int buttonMode = ANON_MODE;

    private static final String TAG = "FSignIn";
    private TextView userText;
    private TextView statusText;
    private EditText emailText;
    private EditText passwordText;
    private Button softButton;

    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText = (TextView) findViewById(R.id.userText);
        statusText = (TextView) findViewById(R.id.statusText);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        softButton = (Button) findViewById(R.id.softButton);

        fbAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    userText.setText(user.getEmail());
                    statusText.setText("Signed In");

                } else {
                    userText.setText("");
                    statusText.setText("Signed Out");
                    softButton.setText("Anonymous Sign-in");
                    buttonMode = ANON_MODE;
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            fbAuth.removeAuthStateListener(authListener);
        }
    }
    public void softButtonClick(View view) {
        if (buttonMode == ANON_MODE) {
            FSignIn();
        } else {
            createAccount();
        }
    }

    public void FSignIn() {
        fbAuth.signInAnonymously()
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(Login.this,
                                            "Authentication failed. "
                                                    + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    softButton.setText("Create an Account");
                                    buttonMode = CREATE_MODE;
                                }
                            }
                        });
    }
    public void createAccount() {

    }

    public void signOut(View view) {
        fbAuth.signOut();
    }
}