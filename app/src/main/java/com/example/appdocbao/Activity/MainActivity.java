package com.example.appdocbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.appdocbao.BroadcastReceiver.Internet;
import com.example.appdocbao.Fragment.GifFragment;
import com.example.appdocbao.Fragment.NewsFragment;
import com.example.appdocbao.Fragment.ProfileFragment;
import com.example.appdocbao.Fragment.TrendFragment;
import com.example.appdocbao.Fragment.UserFragment;
import com.example.appdocbao.R;
import com.example.appdocbao.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DrawerLayout drawerLayout;
    private Internet internetBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        internetBroadcastReceiver = new Internet();

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
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    replaceFragement(new UserFragment());
                }else{
                    replaceFragement(new ProfileFragment());
                }
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