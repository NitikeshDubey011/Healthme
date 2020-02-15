package com.google.healthme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.healthme.FragmentsUI.Account;
import com.google.healthme.FragmentsUI.Home;
import com.google.healthme.FragmentsUI.MyAppointments;
import com.google.healthme.FragmentsUI.MyBlogs;
import com.google.healthme.FragmentsUI.MyHealth;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
//    String title = getString(R.string.app_name);
    private FirebaseAuth oAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
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
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        oAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(getApplicationContext(), MainScreen.class));
                    finish();
                }
            }
        };

        final FirebaseUser user  = oAuth.getCurrentUser();
        Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.WHITE);
//        }
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.categories));
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,
                new Home()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            oAuth.signOut();
                LoginManager.getInstance().logOut();
                updateUI();

        }
//        if (id == R.id.emgm) {
////            swapFragment(new Emergency_frag());
//            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        menuItem.setChecked(true);
//        int id = menuItem.getItemId();
//
//        switch (id) {
//            case R.id.nav_home: {
//                Toast.makeText(this, "clikc\", Toast.LENGTH_SHORT).show();
////                swapFragment(new Home(),false);
//                break;
//            }
//            case R.id.nav_menu_my_profile: {
////                swapFragment(new Account(),true);
//                break;
//            }
////            case R.id.nav_wallet: {
////                swapFragment(new Wallet(),true);
////                break;
////            }
////            case R.id.nav_notification: {
////                break;
////            }
//            case R.id.nav_logout: {
//                oAuth.signOut();
//                LoginManager.getInstance().logOut();
//                updateUI();
//                break;
//            }
//
//        }
////        if (getSupportActionBar() != null) {
////            getSupportActionBar().setTitle(title);
////        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
////    onNavigationItemSelected
//public void swapFragment(Fragment fragment, boolean addToBackStack) {
//    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//    transaction.replace(R.id.nav_host_fragment, fragment);
//    if (addToBackStack)
//        transaction.addToBackStack(null);
//    transaction.commit();
//}
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = oAuth.getCurrentUser();
        if (currentUser == null) {
            updateUI();
        }
    }

    private void updateUI() {
        Toast.makeText(this, "You're logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, MainScreen.class));
        finish();
    }

}
