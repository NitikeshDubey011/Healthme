package com.google.healthme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.healthme.Common.Common;
import com.google.healthme.FragmentsUI.Account;
import com.google.healthme.FragmentsUI.Home;
import com.google.healthme.FragmentsUI.MyAppointments;
import com.google.healthme.FragmentsUI.MyBlogs;
import com.google.healthme.FragmentsUI.MyHealth;
import com.google.healthme.Interface.ItemClickListener;
import com.google.healthme.Model.CategoryModel;
import com.google.healthme.ViewHolder.CategoryViewHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
