package com.example.appdocbao.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdocbao.Activity.LoginActivity;
import com.example.appdocbao.Activity.NewspaperPostingActivity;
import com.example.appdocbao.Activity.SignupActivity;
import com.example.appdocbao.R;


public class ProfileFragment extends Fragment {
    Button btnLogin, btnRegister;
    ImageView avatar;
    TextView manageArticle, userVoucher, tinganday, tindaluu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,
                container, false);

        btnLogin = view.findViewById(R.id.btnLogin_user);
        btnRegister = view.findViewById(R.id.btnRegister_user);
        avatar = view.findViewById(R.id.avatar);

        // Kiểm tra chế độ Dark Mode
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Đang ở chế độ Dark Mode
            avatar.setImageResource(R.drawable.user_icon_white);
        } else {
            // Không ở chế độ Dark Mode
            avatar.setImageResource(R.drawable.user_profile_icon);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manageArticle = view.findViewById(R.id.manageArticle);
        userVoucher = view.findViewById(R.id.userVoucher);
        tinganday = view.findViewById(R.id.tinganday);
        tindaluu = view.findViewById(R.id.tindaluu);

        manageArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Bạn cần đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        userVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Bạn cần đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tinganday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Bạn cần đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
         tindaluu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(getContext(), "Bạn cần đăng nhập để có thể sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(getActivity(), LoginActivity.class);
                 startActivity(intent);
             }
         });
    }
}