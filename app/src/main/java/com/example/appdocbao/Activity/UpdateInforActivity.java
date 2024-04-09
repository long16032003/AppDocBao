package com.example.appdocbao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.appdocbao.Fragment.UserFragment;
import com.example.appdocbao.R;

public class UpdateInforActivity extends AppCompatActivity {

    Button btnSettingAccount;
    ImageView imgUpload, imgback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor);
        btnSettingAccount = (Button) findViewById(R.id.btnSettingAccount);
        btnSettingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(UpdateInforActivity.this, UserFragment.class);
                startActivity(itn);
            }
        });
    }
}