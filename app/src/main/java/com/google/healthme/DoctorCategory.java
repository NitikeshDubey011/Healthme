package com.google.healthme;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.healthme.FragmentsUI.Account;
import com.google.healthme.FragmentsUI.Home;
import com.google.healthme.FragmentsUI.MyAppointments;
import com.google.healthme.FragmentsUI.MyBlogs;
import com.google.healthme.FragmentsUI.MyHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DoctorCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bottom);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                new Home()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new Home();
                            break;
                        case R.id.navigation_health:
                            selectedFragment = new MyHealth();
                            break;
                        case R.id.navigation_appts:
                            selectedFragment = new MyAppointments();
                            break;
                        case R.id.navigation_blogs:
                            selectedFragment = new MyBlogs();
                            break;
                        case R.id.navigation_account:
                            selectedFragment = new Account();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            selectedFragment).commit();
                    return true;
                }
            };

}
