package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.example.appdocbao.R;


import java.util.*;
import java.text.SimpleDateFormat;


public class DetailArticleActivity extends AppCompatActivity {

    ImageView backMainActivity;
    TextView detailTitle, detailContent, detailAuthor, detailDate;
    ImageView detailImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        backMainActivity = findViewById(R.id.imageView6);
        backMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailArticleActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        detailTitle = findViewById(R.id.detailTitle);
        detailContent = findViewById(R.id.detailContent);
        detailImage = findViewById(R.id.detailImage);
        detailAuthor = findViewById(R.id.detailAuthor);
        detailDate = findViewById(R.id.detailDate);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailTitle.setText(bundle.getString("Title"));
            detailContent.setText(bundle.getString("Content"));
            detailAuthor.setText(bundle.getString("Author"));

            long timestamp = bundle.getLong("Date");
            Date date = new Date(timestamp);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
            String formattedDateTime = dateFormat.format(date);
            detailDate.setText(formattedDateTime);

            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
    }
}
