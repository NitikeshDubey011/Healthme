package com.google.healthme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private TextView registerText, forgotText;
    private CheckBox remember_me;
    private FirebaseAuth auth;
    private ProgressBar bar;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.createpassword);
//        remember_me = findViewById(R.id.remember);
        forgotText = findViewById(R.id.forgot);
        auth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.progressBar);
        login = findViewById(R.id.login);
        registerText = findViewById(R.id.text1);

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, DoctorPatient.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_str = email.getText().toString();
                final String password_str = password.getText().toString();

                if (TextUtils.isEmpty(email_str)) {
                    Toast.makeText(Login.this, "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password_str)) {
                    Toast.makeText(Login.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                bar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email_str, password_str).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        bar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            if (password_str.length() < 12) {
                                password.setError(getString(R.string.minimum_password));
                            } else {
                                password.setError(getString(R.string.auth_failed));
                            }
                        } else {
                            startActivity(new Intent(Login.this, DoctorCategory.class));
                            finish();
                        }
                    }
                });
            }
        });
    }
}
