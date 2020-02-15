package com.google.healthme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.healthme.Adapter.SliderAdapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainScreen extends AppCompatActivity {
    CallbackManager callbackManager;
    ViewPager viewPager;
    TabLayout indicator;
    List<Drawable> color;
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;


//    private URL profileImage;
//    private String first_name, last_name, email, id;
//
    SharedPreferences sp;
    private TextView register;
    private Button facebook, login;
    private FirebaseAuth oAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main_screen);
        register = findViewById(R.id.registerTextView);
        facebook = findViewById(R.id.login_button);
        login = findViewById(R.id.loginButton);
        oAuth = FirebaseAuth.getInstance();
        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        color = new ArrayList<>();
        color.add(getResources().getDrawable(R.drawable.a));
        color.add(getResources().getDrawable(R.drawable.b));
        color.add(getResources().getDrawable(R.drawable.c));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, DoctorPatient.class));
            }
        });
        viewPager.setAdapter(new SliderAdapter(this, color));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
//        pref = getApplicationContext().getSharedPreferences("socialData", 0); // 0 - for private mode
//        editor = pref.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, Login.class));
            }
        });

        facebook = findViewById(R.id.login_button);
        printKeyHash();
        callbackManager = CallbackManager.Factory.create();
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebook.setEnabled(false);
                login.setEnabled(false);
                LoginManager.getInstance().logInWithReadPermissions(
                        MainScreen.this,
                        Arrays.asList("email", "public_profile"));


                LoginManager.getInstance().registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {

                                Log.d("login", "facebook:onSuccess: " + loginResult);
                                handleAccessFacebookToken(loginResult.getAccessToken());
//                                // Handle success
//                                mDialog = new ProgressDialog(MainScreen.this);
//                                mDialog.setMessage("Retrieving data...");
//                                mDialog.show();
//                                System.out.println("onSuccess");
//                                accessToken = loginResult.getAccessToken().getToken();
//                                Log.i("accessToken", accessToken);

//                        updateUI();
//                                GraphRequest request = GraphRequest.newMeRequest(
//                                        loginResult.getAccessToken(),
//                                        new GraphRequest.GraphJSONObjectCallback() {
//                                            @Override
//                                            public void onCompleted(JSONObject object,
//                                                                    GraphResponse response) {
//                                                Log.i("LoginActivity",
//                                                        response.toString());
//                                                try {
//                                                    id = object.getString("id");
//                                                    try {
//                                                        URL profile_pic = new URL(
//                                                                "http://graph.facebook.com/" + id + "/picture?width=250&height=250");
//                                                        Log.i("profile_pic",
//                                                                profile_pic + "");
//                                                        goToMainActivity();
//                                                        sp.edit().putBoolean("logged", true).apply();
//
//                                                    } catch (MalformedURLException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    Log.e("UserDate", String.valueOf(object));
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        });


//                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                mDialog.dismiss();
//                                status = true;
//                                Log.d("response", object.toString());
////                                getData(object);
//
//                            }
//                        });

//                                Bundle parameters = new Bundle();
//                                parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
//                                request.setParameters(parameters);
//                                request.executeAsync();
//                                mDialog.dismiss();
                            }

                            @Override
                            public void onCancel() {
                                facebook.setEnabled(true);
                                login.setEnabled(true);
                                Log.d("Login", "facebook:onCancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                facebook.setEnabled(true);
                                login.setEnabled(true);
                                Log.d("Login", "facebook:onError");
                                Log.v("LoginActivity", exception.getCause().toString());
                            }
                        }
                );
            }
        });


// password: CGdL2ZwD8vAVRrkP
// email: vikram16080@gmail.com

//        SharedPreferences sharedPreferences = getSharedPreferences("uniqueKey", 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        TextView textunderlogo = findViewById(R.id.textunderlogo);
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Roboto.ttf");
//        textunderlogo.setTypeface(typeface);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = oAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

//    private void getData(JSONObject obj) {
//        try {
//            first_name = obj.getString("first_name");
//            last_name = obj.getString("last_name");
//            email = obj.getString("email");
//            id = obj.getString("id");
//
//            profileImage = new URL("https://graph.facebook.com/" + id + "/picture?width=250&height=250");
//            editor.putString("firstname", first_name);
//            editor.putString("lastname", last_name);
//            editor.putString("email", email);
//            editor.putString("profileimage", String.valueOf(profileImage));
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.google.healthme", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest digest = MessageDigest.getInstance("SHA");
                digest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(digest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {
        Toast.makeText(this, "You're logged in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainScreen.this, MainActivity.class));
        finish();

    }

    private void handleAccessFacebookToken(AccessToken accessToken) {
        Log.d("Login", "handleFacebookAccessToken: " + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        oAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Login", "signInWithCredential:success");
                            FirebaseUser user = oAuth.getCurrentUser();
                            updateUI();

                        } else {
                            Log.d("Login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainScreen.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < color.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
