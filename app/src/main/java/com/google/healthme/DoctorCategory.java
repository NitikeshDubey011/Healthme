package com.google.healthme;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.healthme.FragmentsUI.Account;
import com.google.healthme.FragmentsUI.Home;
import com.google.healthme.FragmentsUI.MyAppointments;
import com.google.healthme.FragmentsUI.MyBlogs;
import com.google.healthme.FragmentsUI.MyHealth;

public class DoctorCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bottom);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Paper.init(this);
//        String language=Paper.book().read("language");
//        if (language == null) {
//
//            Paper.book().write("language","en");
//
//        }
//        updateView((String)Paper.book().read("language"));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.categories));
        NavigationView navigationView = findViewById(R.id.nav_view_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
