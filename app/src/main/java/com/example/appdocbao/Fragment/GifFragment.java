package com.example.appdocbao.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Activity.UserActivity;
import com.example.appdocbao.Activity.ViewPagerPhoto;
import com.example.appdocbao.Adapter.RecentlyReadAdapter;
import com.example.appdocbao.Adapter.ViewPagerPhotoAdapter;
import com.example.appdocbao.Adapter.VoucherAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.Model.Voucher;
import com.example.appdocbao.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GifFragment extends Fragment {

    private RecyclerView rcvGifFragment;
    private VoucherAdapter voucherAdapter;
    private ArrayList<Voucher> listVoucher;
    DatabaseReference voucherReference, userReference;
    ValueEventListener eventListener;

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private List<ViewPagerPhoto> mListPhoto;
    private Handler mHandler = new Handler();
    private TextView pointUser;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getCurrentItem() == mListPhoto.size() - 1){
                mViewPager.setCurrentItem(0);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvGifFragment = view.findViewById(R.id.rcvVoucher);
        mViewPager = view.findViewById(R.id.view_pager);
        mCircleIndicator = view.findViewById(R.id.cicle_indicator);
        pointUser = view.findViewById(R.id.pointuser);

        mListPhoto = getListPhoto();
//        progressIndicator = findViewById(R.id.progress_bar);
        setupRecycleView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        ViewPagerPhotoAdapter adapter = new ViewPagerPhotoAdapter(mListPhoto);
        mViewPager.setAdapter(adapter);

        mCircleIndicator.setViewPager(mViewPager);
        mHandler.postDelayed(mRunnable, 3000);

        listVoucher = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(getContext(), listVoucher);
        rcvGifFragment.setAdapter(voucherAdapter);

        voucherReference = FirebaseDatabase.getInstance().getReference("voucher");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();

        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy dữ liệu người dùng từ dataSnapshot
                int point = dataSnapshot.child("points").getValue(Integer.class);

                pointUser.setText("Điểm tích lũy: " + point);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Xử lí lỗi
            }
        });


        eventListener = voucherReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listVoucher.clear();
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    Voucher voucher = itemSnapShot.getValue(Voucher.class);
                    listVoucher.add(voucher);
                }
                voucherAdapter.notifyDataSetChanged();
//                changeInProgress(false);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<ViewPagerPhoto> getListPhoto() {
        List<ViewPagerPhoto> list = new ArrayList<>();
        list.add(new ViewPagerPhoto(R.drawable.tron_japan));
        list.add(new ViewPagerPhoto(R.drawable.tron_viet_name));

        return list;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 3000);
    }

    void setupRecycleView(){
        rcvGifFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvGifFragment.setHasFixedSize(true);
        voucherAdapter = new VoucherAdapter(getContext(),listVoucher);
        rcvGifFragment.setAdapter(voucherAdapter);
    }
}