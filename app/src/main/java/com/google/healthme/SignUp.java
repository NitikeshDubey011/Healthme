package com.google.healthme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msg91.sendotpandroid.library.SendOtpVerification;
import com.msg91.sendotpandroid.library.Verification;
import com.msg91.sendotpandroid.library.VerificationListener;

import java.util.Random;

public class SignUp extends AppCompatActivity implements VerificationListener {

    private Button signup;
    private EditText email, password, mobile_number, confirm_password;
    private SharedPreferences storage;
    private SharedPreferences.Editor editor;
    private String TAG = "Message";
    private Verification mVerification;
    private String countryCode = "+91";
    String Otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.createpassword);
        mobile_number = findViewById(R.id.mobileNumber);
        confirm_password = findViewById(R.id.confirmpassword);
        signup = findViewById(R.id.submit);


        storage = getApplicationContext().getSharedPreferences("OTP", MODE_PRIVATE);
        editor = storage.edit();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your email address", Toast.LENGTH_SHORT).show();

                } else if (mobile_number.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();

                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your password", Toast.LENGTH_SHORT).show();

                } else if (confirm_password.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your confirm password", Toast.LENGTH_SHORT).show();

                } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    Toast.makeText(SignUp.this, "Please make sure your both passwords are same", Toast.LENGTH_SHORT).show();
                } else if (!(email.getText().toString().equals("") &&
                        mobile_number.getText().toString().equals("") &&
                        password.getText().toString().equals("") &&
                        confirm_password.getText().toString().equals(""))) {
//                    OtpScreen otpScreen=new OtpScreen();
//                    FragmentManager fragmentManager=getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, otpScreen).commit();
                    startActivity(new Intent(SignUp.this,Otp.class));
                    getRandomNumberString();
                    mVerification = SendOtpVerification.createSmsVerification
                            (SendOtpVerification
                                    .config(countryCode + mobile_number.getText().toString())

                                    .context(getApplicationContext())
                                    .unicode(false)
                                    .autoVerification(true)
                                    .httpsConnection(false)
                                    .expiry("15")
                                    .senderId("HLTHME")
                                    .otplength("6")
                                    .message("Your HealthMe OTP for login is " + Otp + ". This OTP will expire in 15 minutes.")
                                    .build(), SignUp.this);
                    mVerification.initiate();


                }
            }
        });


    }

    public void getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        Otp = String.format("%06d", number);
        editor.putString("otp_number", Otp);
        editor.putString("mobile_number",mobile_number.getText().toString());
        editor.apply();

    }

//    public void swap_fragment(Fragment fragment) {
//        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }

    @Override
    public void onInitiated(String response) {
        Log.d(TAG, "Initialized!" + response);
        //OTP successfully resent/sent.
    }

    //onInitiationFailed call  when failed to initialized
    @Override
    public void onInitiationFailed(Exception exception) {
        Log.e(TAG, "Verification initialization failed: " + exception.getMessage());
        //sending otp failed.
    }

    //onVerified call  when direct verirfication success
    @Override
    public void onVerified(String response) {
        Log.d(TAG, "Verified!\n" + response);
        //OTP verified successfully.
    }

    @Override
    public void onVerificationFailed(Exception exception) {
        Log.e(TAG, "Verification failed: " + exception.getMessage());
        //OTP  verification failed.
    }

}
