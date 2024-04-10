package com.example.appdocbao.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdocbao.Adapter.VoucherChangeAdapter;
import com.example.appdocbao.Model.Voucher;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoucherChangeActivity extends AppCompatActivity {
    ImageView backarrow;
    private RecyclerView rcvVoucherFragment;
    private VoucherChangeAdapter voucherChangeAdapter;
    private ArrayList<Voucher> listVoucher;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_change);
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvVoucherFragment = findViewById(R.id.voucherChange);
        databaseReference = FirebaseDatabase.getInstance().getReference("voucher");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvVoucherFragment.setLayoutManager(layoutManager);

        listVoucher = new ArrayList<>();
        voucherChangeAdapter = new VoucherChangeAdapter(this, listVoucher);
        rcvVoucherFragment.setAdapter(voucherChangeAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        String id_User = "";
        if(user != null){
            id_User = user.getUid();
        }else if(googleSignInAccount!=null){
            id_User = googleSignInAccount.getId();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+id_User+"/voucher");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listVoucher.clear();
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    Voucher voucher = itemSnapShot.getValue(Voucher.class);
                    listVoucher.add(voucher);
                }

                voucherChangeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
