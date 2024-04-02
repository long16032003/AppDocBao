package com.example.appdocbao.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdocbao.Adapter.VoucherChangeAdapter;
import com.example.appdocbao.Model.Voucher;
import com.example.appdocbao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoucherChangeFragment extends Fragment {
    ImageView backarrow;
    private RecyclerView rcvVoucherFragment;
    private VoucherChangeAdapter voucherChangeAdapter;
    private ArrayList<Voucher> listVoucher;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voucher_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backarrow = view.findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragment userFragment = new UserFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout,userFragment).commit();
            }
        });
        rcvVoucherFragment = view.findViewById(R.id.voucherChange);
        databaseReference = FirebaseDatabase.getInstance().getReference("voucher");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvVoucherFragment.setLayoutManager(layoutManager);

        listVoucher = new ArrayList<>();
        voucherChangeAdapter = new VoucherChangeAdapter(getContext(), listVoucher);
        rcvVoucherFragment.setAdapter(voucherChangeAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id_User = user.getUid();

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