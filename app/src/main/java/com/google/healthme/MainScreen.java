package com.google.healthme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    private TextView register;
    private Button facebook, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        register=findViewById(R.id.registerTextView);
        facebook=findViewById(R.id.facebookButton);
        login=findViewById(R.id.loginButton);
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainScreen.this, "Login button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,SignUp.class));
            }
        });




//        SharedPreferences sharedPreferences=getSharedPreferences("uniqueKey",0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        TextView textunderlogo=findViewById(R.id.textunderlogo);
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Roboto.ttf");
//        textunderlogo.setTypeface(typeface);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(MainScreen.this, SignUp.class);
//                startActivity(i);
//                finish();
//            }
//        }, 2000);


    }

//    public void swap_fragment(Fragment fragment){
//        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }
}
