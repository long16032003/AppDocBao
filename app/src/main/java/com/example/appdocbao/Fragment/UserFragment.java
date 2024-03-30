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

import com.example.appdocbao.Activity.MainActivity;
import com.example.appdocbao.Activity.NewspaperPostingActivity;
import com.example.appdocbao.Activity.UserActivity;
import com.example.appdocbao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    Button btnActivityPostArticle, btnLogOut;
    TextView userName, email;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = mAuth.getCurrentUser();
        String idUser = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setContentView(R.layout.activity_user);
        View view = inflater.inflate(R.layout.fragment_user, container, false);

//        userName = getView().findViewById(R.id.userName);
//        email = getView().findViewById(R.id.email);
//        btnActivityPostArticle = getView().findViewById(R.id.btnActivityPostArticle);
//        btnLogOut = getView().findViewById(R.id.btnLogOut);

        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        btnActivityPostArticle = view.findViewById(R.id.btnActivityPostArticle);
        btnLogOut = view.findViewById(R.id.btnLogOut);

        FirebaseUser user = mAuth.getCurrentUser();
        String idUser = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
        // Inflate the layout for this fragment
        return view;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Phân giải các phần tử giao diện người dùng
        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        btnActivityPostArticle = view.findViewById(R.id.btnActivityPostArticle);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
                // Lấy dữ liệu người dùng từ dataSnapshot
                String name = dataSnapshot.child("name").getValue(String.class);
                String emailSnapshot = dataSnapshot.child("email").getValue(String.class);
                userName.setText(name);
                email.setText(emailSnapshot);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Xử lí lỗi
            }
        });
//        btnActivityPostArticle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserActivity.this, NewspaperPostingActivity.class);
//                startActivity(intent);
//            }
//        }) ;
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut(); // Đăng xuất, sau sẽ chuyển vào sự kiện của nút đăng xuất
//                Toast.makeText(UserActivity.this,"Đã đăng xuất",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(UserActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
        btnActivityPostArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewspaperPostingActivity.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Đăng xuất, sau sẽ chuyển vào sự kiện của nút đăng xuất
                Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}