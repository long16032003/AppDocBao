package com.example.appdocbao.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Activity.MainActivity;
import com.example.appdocbao.Activity.NewspaperPostingActivity;
import com.example.appdocbao.Activity.RecentlyReadActivity;
import com.example.appdocbao.Activity.SavedArticlesActivity;
import com.example.appdocbao.Activity.UserActivity;
import com.example.appdocbao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    Button btnActivityPostArticle, btnLogOut;
    TextView userName, email, pointUser, tinganday, tindaluu;
    CircleImageView profilePicture;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        // Inflate the layout for this fragment
        return view;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        pointUser = view.findViewById(R.id.pointUser);
        btnActivityPostArticle = view.findViewById(R.id.btnActivityPostArticle);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        tinganday = view.findViewById(R.id.tinganday);
        tindaluu = view.findViewById(R.id.tindaluu);
        profilePicture = view.findViewById(R.id.profilePicture);

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
                String imageSnapshot = dataSnapshot.child("img").getValue(String.class);

                if (isAdded()) {
                    Glide.with(requireContext()).load(imageSnapshot).into(profilePicture);
                }
                userName.setText(name);
                email.setText(emailSnapshot);
                pointUser.setText("Quản lý bài đăng");
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
                Intent intent = new Intent(getContext(), NewspaperPostingActivity.class);
                startActivity(intent);
            }
        });
        tindaluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SavedArticlesActivity.class);
                startActivity(intent);
            }
        });
        tinganday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecentlyReadActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Đăng xuất, sau sẽ chuyển vào sự kiện của nút đăng xuất
                Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}