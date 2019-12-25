package com.google.healthme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainScreen extends AppCompatActivity {
    private TextView register;
    private Button facebook, login;
    private ProgressDialog mDialog;
    private boolean status = false;
    CallbackManager callbackManager;

    private  URL profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main_screen);
        register = findViewById(R.id.registerTextView);
        facebook = findViewById(R.id.login_button);
        login = findViewById(R.id.loginButton);
        facebook = findViewById(R.id.login_button);
        printKeyHash();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success
                        mDialog = new ProgressDialog(MainScreen.this);
                        mDialog.setMessage("Retrieving data...");
                        mDialog.show();

                        String accessToken = loginResult.getAccessToken().getToken();
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                mDialog.dismiss();
                                status = true;
                                Log.d("response",object.toString());
                                getData(object);

                            }
                        });

                        Bundle parameters=new Bundle();
                        parameters.putString("fields","id,email,first_name,last_name");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                }
        );
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainScreen.this, "Login button clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainScreen.this,SignUp.class));
//            }
//        });


// password: CGdL2ZwD8vAVRrkP
// email: vikram16080@gmail.com

        SharedPreferences sharedPreferences = getSharedPreferences("uniqueKey", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TextView textunderlogo = findViewById(R.id.textunderlogo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Roboto.ttf");
        textunderlogo.setTypeface(typeface);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(MainScreen.this, SignUp.class);
//                startActivity(i);
//                finish();
//            }
//        }, 2000);

//if (AccessToken.getCurrentAccessToken()!=null){
////    txtEmail.setText(AccessToken.getCurrentAccessToken().getUserId());

//}
    }

    private void getData(JSONObject obj) {
        try {
            profileImage = new URL("https://graph.facebook.com/" + obj.getString("id") + "/picture?width=250&height=250");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        final MenuItem settingsItem = menu.findItem(R.id.navigation_account);
//        if (status) {
//            Glide.with(this)
//                    .load(profileImage)
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>(24, 24) {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            settingsItem.setIcon(new BitmapDrawable(getResources(), resource));
//                        }
//
//                    });
//        }
//        return super.onPrepareOptionsMenu(menu);
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

//    public void swap_fragment(Fragment fragment){
//        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }

    public void onClick(View v) {
        if (v == facebook) {
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
