package com.example.appdocbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appdocbao.R;

public class DetailImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_img);
        ImageView fullImageView = findViewById(R.id.fullImageView);
        ImageButton closeButton = findViewById(R.id.closeButton);
        Intent it = getIntent();
        String imgurl = it.getStringExtra("Image");
        Glide.with(this).load(imgurl).into(fullImageView);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}