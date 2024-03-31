package com.example.appdocbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.Model.Voucher;
import com.example.appdocbao.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private Context mContext;

    private ArrayList<Voucher> mListVoucher;

    public VoucherAdapter(Context mContext, ArrayList<Voucher> mListVoucher) {
        this.mContext = mContext;
        this.mListVoucher = mListVoucher;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View voucherView = inflater.inflate(R.layout.item_voucher_view, parent, false);
        VoucherViewHolder viewHolder = new VoucherViewHolder(voucherView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = mListVoucher.get(position);
        if(voucher == null){
            return;
        }
        holder.title.setText(voucher.getTitle());
        Glide.with(mContext)
                .load(voucher.getImg())
                .into(holder.imgvoucher);
        holder.point.setText(voucher.getAchievePoints() + " Điểm");
        long timestamp = voucher.getExpiryTimestamp();
        Date dateTime = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDateTime = dateFormat.format(dateTime);
        holder.expiryDate.setText(formattedDateTime);
    }

    @Override
    public int getItemCount() {
        return mListVoucher.size();
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        ImageView imgvoucher;
        TextView title, expiryDate, point;
        Button btnRedeem;
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvoucher = itemView.findViewById(R.id.voucher);
            title = itemView.findViewById(R.id.title);
            expiryDate = itemView.findViewById(R.id.expirydate);
            point = itemView.findViewById(R.id.point);
            btnRedeem = itemView.findViewById(R.id.btnRedeem);
        }
    }
}

