package com.example.appdocbao.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdocbao.Fragment.GifFragment;
import com.example.appdocbao.Fragment.NewsFragment;
import com.example.appdocbao.Fragment.ProfileFragment;
import com.example.appdocbao.Fragment.TrendFragment;
import com.example.appdocbao.R;
import com.example.appdocbao.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        drawerLayout = findViewById(R.id.drawerlayout);
//        NavigationView navigationView = findViewById(R.id.navigation_view);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.close_nav, R.string.open_nav);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if(item.getItemId()==R.id.setting){
//                    Toast.makeText(MainActivity.this,"Setting",Toast.LENGTH_SHORT).show();
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                }else if(item.getItemId()==R.id.setting) {
//                    Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();
//                    drawerLayout.closeDrawer(GravityCompat.START);
//
//                }else if(item.getItemId()==R.id.setting) {
//                    Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();
//                    drawerLayout.closeDrawer(GravityCompat.START);
//
//                }
//                return true;
//            }
//        });


        replaceFragement(new NewsFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.news) {
                replaceFragement(new NewsFragment());
            } else if (itemId == R.id.gif) {
                replaceFragement(new GifFragment());
            } else if (itemId == R.id.trend) {
                replaceFragement(new TrendFragment());
            } else if (itemId == R.id.profile) {
                replaceFragement(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragement(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}