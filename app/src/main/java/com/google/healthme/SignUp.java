package com.google.healthme;

import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;
import com.msg91.sendotpandroid.library.SendOtpVerification;
import com.msg91.sendotpandroid.library.Verification;
import com.msg91.sendotpandroid.library.VerificationListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements VerificationListener {

    private Button signup;
    private EditText email, password, mobile_number, confirm_password;
    private SharedPreferences storage;
    private SharedPreferences.Editor editor;
    private String TAG = "Message";
    private Verification mVerification;
    private String countryCode = "+91";

    private ProgressBar bar;

    String Otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.createpassword);
        mobile_number = findViewById(R.id.mobileNumber);
        confirm_password = findViewById(R.id.confirmpassword);
        signup = findViewById(R.id.register);
        bar = findViewById(R.id.progressBar);
//        prefs=getApplicationContext().getSharedPreferences("signup_data",0);
//        editor=prefs.edit();


        storage = getApplicationContext().getSharedPreferences("OTP", MODE_PRIVATE);
        editor = storage.edit();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomNumberString();
                if (mobile_number.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();

                } else if (isValidPhone(mobile_number.getText().toString())) {
                    Toast.makeText(SignUp.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your email address", Toast.LENGTH_SHORT).show();

                } else if (password.getText().toString().equals("") || (password.getText().toString().length() < 12 && !isValidPassword(password.getText().toString()))) {
                    Toast.makeText(SignUp.this, "Please enter valid password i.e. password should contain (A-Z, a-b, 0-9) and (~! @#$%^&*)", Toast.LENGTH_SHORT).show();

                } else if (confirm_password.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please enter your password again", Toast.LENGTH_SHORT).show();

                } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    Toast.makeText(SignUp.this, "Please make sure your both passwords are same", Toast.LENGTH_SHORT).show();
                } else if (!(email.getText().toString().equals("") &&
                        mobile_number.getText().toString().equals("") &&
                        password.getText().toString().equals("") &&
                        confirm_password.getText().toString().equals(""))) {

                    bar.setVisibility(View.VISIBLE);


                    startActivity(new Intent(SignUp.this, Otp.class));

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
                                    .otp(Otp)
                                    .message("Your HealthMe OTP for login is " + Otp + ". This OTP will expire in 15 minutes.")
                                    .build(), SignUp.this);
                    mVerification.initiate();

                    finish();
                }

//                if(password.getText().toString().length()<12 &&!isValidPassword(password.getText().toString())){
//                    System.out.println("Password is not valid");
//                }else{
//                    System.out.println("Valid");
//                }
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
        editor.putString("mobile_number", mobile_number.getText().toString());
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
        editor.putString("mb_number",mobile_number.getText().toString());
        editor.putString("em_address",email.getText().toString());
        editor.putString("pass",password.getText().toString());
        editor.apply();



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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isValidPhone(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^([0-9])";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    protected void onResume() {
        super.onResume();
        bar.setVisibility(View.GONE);
    }
}
