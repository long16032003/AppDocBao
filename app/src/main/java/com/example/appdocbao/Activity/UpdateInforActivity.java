package com.example.appdocbao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.appdocbao.Fragment.UserFragment;
import com.example.appdocbao.R;

public class UpdateInforActivity extends AppCompatActivity {

    ImageView backImg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor);
        backImg = (ImageView) findViewById(R.id.backImg);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(UpdateInforActivity.this, UserFragment.class);
                startActivity(itn);
            }
        });
    }
}