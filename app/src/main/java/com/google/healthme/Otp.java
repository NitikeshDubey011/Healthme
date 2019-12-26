package com.google.healthme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.msg91.sendotpandroid.library.SendOtpVerification;
import com.msg91.sendotpandroid.library.Verification;
import com.msg91.sendotpandroid.library.VerificationListener;

import java.util.Locale;
import java.util.Random;

public class Otp extends AppCompatActivity  implements VerificationListener {
    private static final long START_TIME_IN_MILLIS = 25000;

    private Button submitOTP,otpResend;
    private EditText otpEnter;
    private TextView timer,textView,phNumber;
    private SharedPreferences otp_number;
    private String otp_number_six,mobile_number;
    private int seconds=0;
    private CountDownTimer mCountDownTimer;
    private String TAG = "Message";
    private Verification mVerification;
    private String countryCode = "+91";
    String Otp;
    private SharedPreferences storage;
    private SharedPreferences.Editor editor;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private SharedPreferences otp_number_2;
    private String otp_number_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpEnter = findViewById(R.id.otp);
        otpResend = findViewById(R.id.otpResend);
        submitOTP=findViewById(R.id.submit);
        timer=findViewById(R.id.timer);
        textView=findViewById(R.id.txtData);
        phNumber=findViewById(R.id.phNumber);
        otpResend.setEnabled(false);
        storage = getApplicationContext().getSharedPreferences("OTP2", MODE_PRIVATE);
        editor = storage.edit();
        startTimer();

        otp_number =getSharedPreferences("OTP", Context.MODE_PRIVATE);
        otp_number_2=getSharedPreferences("OTP2", Context.MODE_PRIVATE);
        otp_number_six= otp_number.getString("otp_number", null);
        mobile_number=otp_number.getString("mobile_number",null);
        Toast.makeText(Otp.this, otp_number+"", Toast.LENGTH_SHORT).show();

//        if (otp_number_six.equals(otpEnter.getText().toString()))
        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp_number_six.equals(otpEnter.getText().toString()) || otp_number_again.equals(otpEnter.getText().toString())){
                    Toast.makeText(Otp.this, "Account Verification Done!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Otp.this, DoctorCategory.class));
                    finish();
                }
                else{
                    Toast.makeText(Otp.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    otpEnter.setText("");
                }
            }
        });
        otpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Otp.this, "You will get your OTP in a moment", Toast.LENGTH_SHORT).show();
                resetTimer();
                getRandomNumberString();
                otp_number_again=otp_number_2.getString("otp_number",null);
                mVerification = SendOtpVerification.createSmsVerification
                        (SendOtpVerification
                                .config(countryCode + mobile_number)

                                .context(getApplicationContext())
                                .unicode(false)
                                .autoVerification(true)
                                .httpsConnection(false)
                                .expiry("15")
                                .senderId("HLTHME")
                                .otplength("6")
                                .otp(Otp)
                                .message("Your OTP for login is " + Otp + ". This OTP will expire in 15 minutes.")
                                .build(), Otp.this);
                mVerification.resend("text");
            }
        });
        phNumber.setText(mobile_number);

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        startTimer();
        timer.setVisibility(View.VISIBLE);
        textView.setText("Please wait for OTP ");
        otpResend.setEnabled(false);
    }

    private void updateCountDownText() {
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        timer.setText(timeLeftFormatted+"s");
        if (timeLeftFormatted.equals("00")){
            timer.setVisibility(View.GONE);
            textView.setText("Didn't got the code? ");
            otpResend.setEnabled(true);
        }


    }

    public void getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        Otp = String.format("%06d", number);
        Toast.makeText(this, Otp + "", Toast.LENGTH_SHORT).show();
        editor.putString("otp_number", Otp);
        editor.apply();

    }
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
