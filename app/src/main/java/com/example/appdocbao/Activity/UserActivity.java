package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdocbao.Fragment.TrendFragment;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    Button btnActivityPostArticle, btnLogOut;
    TextView userName, email, pointUser, tinganday, tindaluu;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        pointUser = findViewById(R.id.pointUser);
        btnActivityPostArticle = findViewById(R.id.btnActivityPostArticle);
        btnLogOut = findViewById(R.id.btnLogOut);
        tinganday = findViewById(R.id.tinganday);
        tindaluu = findViewById(R.id.tindaluu);

        FirebaseUser user = mAuth.getCurrentUser();
        String idUser = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu người dùng từ dataSnapshot
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String emailSnapshot = dataSnapshot.child("email").getValue(String.class);
                    int point = dataSnapshot.child("points").getValue(Integer.class);
                    userName.setText(name);
                    email.setText(emailSnapshot);
                    pointUser.setText("Điểm tích lũy: " + point);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Xử lí lỗi
            }
        });
        btnActivityPostArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, NewspaperPostingActivity.class);
                startActivity(intent);
            }
        }) ;
        tindaluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, SavedArticlesActivity.class);
                startActivity(intent);
            }
        });
        tinganday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, RecentlyReadActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Đăng xuất, sau sẽ chuyển vào sự kiện của nút đăng xuất
                Toast.makeText(UserActivity.this,"Đã đăng xuất",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}