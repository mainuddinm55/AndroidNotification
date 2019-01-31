package com.learner.androidnotification.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.learner.androidnotification.R;

public class MainActivity extends AppCompatActivity {

    // 1. Notification Channel
    // 2. Notification Builder
    // 3. Notification Manager

    public static final String CHANNEL_ID = "android_notification";
    private static final String CHANNEL_NAME = "Android Notification";
    private static final String CHANNEL_DESCRIPTION = "Android push notification from backend server";

    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        Button signUpBtn = findViewById(R.id.singup_btn);
        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().isEmpty()) {
                    emailEditText.setError("Email required");
                    emailEditText.requestFocus();
                    return;
                }
                if (passwordEditText.getText().toString().isEmpty()) {
                    passwordEditText.setError("Password required");
                    passwordEditText.requestFocus();
                    return;
                }
                if (passwordEditText.getText().toString().length() < 6) {
                    passwordEditText.setError("Password must be grater then 6 character");
                    passwordEditText.requestFocus();
                    return;
                }
                createUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

    }

    private void createUser(final String email, final String password) {
        showSpinner();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    hideSpinner();
                    startProfileActivity();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        userLogin(email, password);
                    } else {
                        hideSpinner();
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideSpinner();
                            startProfileActivity();
                        } else {
                            hideSpinner();
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startProfileActivity();
        }
    }
    private void showSpinner() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading.....");
        mDialog.show();
    }

    private void hideSpinner() {
        if (mDialog != null) mDialog.dismiss();
    }


}
