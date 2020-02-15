package com.google.healthme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
    Button sendLink;
    FirebaseAuth firebaseAuth;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email);
        sendLink = findViewById(R.id.sendLink);
        firebaseAuth = FirebaseAuth.getInstance();



        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new ProgressDialog(ForgotPassword.this);
                mDialog.setMessage("Sending reset link...");
                mDialog.show();
                String email_str = email.getText().toString();

                if (TextUtils.isEmpty(email_str)) {
                    Toast.makeText(getApplicationContext(), "Please fill e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

        firebaseAuth.sendPasswordResetEmail(email_str).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    Toast.makeText(ForgotPassword.this, "Password reset link was sent to your email address", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this,MainScreen.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Mail sending error - E-mail not found",Toast.LENGTH_SHORT).show();
                }
            }
        });
            }
        });


    }
}