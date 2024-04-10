package com.example.appdocbao.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Activity.MainActivity;
import com.example.appdocbao.Activity.ManageArticleActivity;
import com.example.appdocbao.Activity.NewspaperPostingActivity;
import com.example.appdocbao.Activity.RecentlyReadActivity;
import com.example.appdocbao.Activity.SavedArticlesActivity;
import com.example.appdocbao.Activity.UpdateInforActivity;
import com.example.appdocbao.Activity.VoucherChangeActivity;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    Button btnActivityPostArticle, btnLogOut, btnSettingAccount;
    TextView userName, email, manageArticle, tinganday, tindaluu, userVoucher;
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
        btnSettingAccount = view.findViewById(R.id.btnSettingAccount);
        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        manageArticle = view.findViewById(R.id.manageArticle);
        btnActivityPostArticle = view.findViewById(R.id.btnActivityPostArticle);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        tinganday = view.findViewById(R.id.tinganday);
        tindaluu = view.findViewById(R.id.tindaluu);
        profilePicture = view.findViewById(R.id.profilePicture);
        userVoucher = view.findViewById(R.id.userVoucher);
        btnSettingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(getContext(), UpdateInforActivity.class);
                startActivity(itn);
            }
        });
        userVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherChangeActivity voucherChangeActivity = new VoucherChangeActivity();
                Intent it = new Intent(getContext(),voucherChangeActivity.getClass());
                startActivity(it);
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            String idUser = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu người dùng từ dataSnapshot
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String emailSnapshot = dataSnapshot.child("email").getValue(String.class);
                    String imageSnapshot = dataSnapshot.child("img").getValue(String.class);

                    if (isAdded()) {
                        Glide.with(requireContext()).load(imageSnapshot).into(profilePicture);
                    }
                    userName.setText(name);
                    email.setText(emailSnapshot);
//                }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Xử lí lỗi
                }
            });
        }
        btnActivityPostArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewspaperPostingActivity.class);
                startActivity(intent);
            }
        });
        manageArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageArticleActivity.class);
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
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(),googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if(googleSignInAccount != null){
            final String getFullname = googleSignInAccount.getDisplayName();
            final String getEmail = googleSignInAccount.getEmail();
            final Uri getPhotoUrl = googleSignInAccount.getPhotoUrl();
            userName.setText(getFullname);
            email.setText(getEmail);
            if (isAdded()) {
                Glide.with(getContext()).load(getPhotoUrl).into(profilePicture);
            }
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
                FirebaseAuth.getInstance().signOut(); // Đăng xuất, sau sẽ chuyển vào sự kiện của nút đăng xuất
                Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);


            }
        });
    }
}