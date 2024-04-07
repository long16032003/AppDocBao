package com.example.appdocbao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Activity.DetailArticleActivity;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class RecyclerArticleAdapter extends RecyclerView.Adapter<RecyclerArticleAdapter.ArticleViewHolder> {
    private Context mContext;
    private ArrayList<Article> mListArticle;
    Article articleRecently;
    private int acountArticleShow;

    public RecyclerArticleAdapter(Context mContext, ArrayList<Article> mListArticle, int acountArticleShow) {
        this.mContext = mContext;
        this.mListArticle = mListArticle;
        this.acountArticleShow = acountArticleShow;
    }
    public void setData(ArrayList<Article> list){
        this.mListArticle = list;
        notifyDataSetChanged();     // thực hiện bind load data vào adapter
    }
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View articleView = inflater.inflate(R.layout.item_articles, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = mListArticle.get(position);
        if(article == null){
            return;
        }
        holder.titleArticle.setText(article.getTitle());
        Glide.with(mContext)
                .load(article.getImg())
                .into(holder.imgArticle);
        holder.authorArticle.setText(article.getAuthor());
        long timestamp = article.getTimestamp();
        Date dateTime = new Date(timestamp);
        String formattedDateTime;

        // Thời gian hiện tại
        Date now = new Date();
        //Tính toán xem thời gian đăng báo cách thời gian hiện tại bao lâu
        long diffInMillis = now.getTime() - timestamp;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if (days < 1) {
            if (hours < 1) {
                // Hiển thị số phút
                formattedDateTime = minutes + " phút trước";
            } else {
                // Hiển thị số giờ
                formattedDateTime = hours + " giờ trước";
            }
        } else {
            // Hiển thị ngày, tháng, năm
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            formattedDateTime = dateFormat.format(dateTime);
        }
        holder.dateArticle.setText(formattedDateTime);
        holder.cardViewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(v.getContext());
//                final String id_User = mAuth!=null ? mAuth.getUid() : (googleSignInAccount != null ? googleSignInAccount.getId() : "" );
                if(mAuth != null){
                    String id_User = mAuth.getUid();
                    //======== Chức năng thêm bài báo đọc gần đây ==========
                    RecentlyReadArticle(id_User,holder);
                    // ============Tích điểm ====================
                    TichDiem(id_User);
                    //==========Tăng view bài báo=================
                    IncreaseViewArticle(article.getId());
                }
                else if(googleSignInAccount!=null){
                    String id_User = googleSignInAccount.getId();
                    //======== Chức năng thêm bài báo đọc gần đây ==========
                    RecentlyReadArticle(id_User,holder);
                    // =========Tích điểm ====================
                    TichDiem(id_User);
                    //==========Tăng view bài báo=================
                    IncreaseViewArticle(article.getId());
                }
                //======================================================
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                intent.putExtra("Id", mListArticle.get(holder.getAdapterPosition()).getId());
                intent.putExtra("Image", mListArticle.get(holder.getAdapterPosition()).getImg());
                intent.putExtra("IdUserPost", mListArticle.get(holder.getAdapterPosition()).getIdUserPost());
                intent.putExtra("idCategory", mListArticle.get(holder.getAdapterPosition()).getCategoryId());
                intent.putExtra("Title", mListArticle.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Content", mListArticle.get(holder.getAdapterPosition()).getContent());
                intent.putExtra("Author", mListArticle.get(holder.getAdapterPosition()).getAuthor());
                intent.putExtra("Date", mListArticle.get(holder.getAdapterPosition()).getTimestamp());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(acountArticleShow >= mListArticle.size()){
            return mListArticle.size();
        }else{
            return acountArticleShow;
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgArticle;
        private TextView titleArticle;
        private CardView cardViewArticle;
        private TextView authorArticle;
        private TextView dateArticle;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArticle = itemView.findViewById(R.id.imgArticle);
            titleArticle = itemView.findViewById(R.id.titleArticle);
            cardViewArticle = itemView.findViewById(R.id.cardViewArticle);
            authorArticle = itemView.findViewById(R.id.authorArticle);
            dateArticle = itemView.findViewById(R.id.dateArticle);
        }
    }
    private void IncreaseViewArticle(String id_Article){
        DatabaseReference viewReference =  FirebaseDatabase.getInstance().getReference("articles/" + id_Article + "/view");
        viewReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                // Lấy giá trị hiện tại của view
                Integer currentView = mutableData.getValue(Integer.class);

                if (currentView != null) {
                    // Tăng giá trị view lên 1
                    mutableData.setValue(currentView + 1);
                }

                // Trả về giá trị mới của points
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (committed) {
                    // Tăng giá trị points thành công
                    // Thực hiện các hành động phụ thuộc vào việc tăng points ở đây
                } else {
                    // Tăng giá trị points thất bại
                    // Xử lý lỗi nếu cần
                }
            }
        });
    }
    private void TichDiem(String id_User){
        DatabaseReference pointReference =  FirebaseDatabase.getInstance().getReference("users/"+id_User+"/points");
        pointReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                // Lấy giá trị hiện tại của points
                Integer currentPoints = mutableData.getValue(Integer.class);

                if (currentPoints != null) {
                    // Tăng giá trị points lên 1
                    mutableData.setValue(currentPoints + 1);
                }

                // Trả về giá trị mới của points
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (committed) {
                    // Tăng giá trị points thành công
                    // Thực hiện các hành động phụ thuộc vào việc tăng points ở đây
                } else {
                    // Tăng giá trị points thất bại
                    // Xử lý lỗi nếu cần
                }
            }
        });
    }
    private void RecentlyReadArticle(String id_User, ArticleViewHolder holder){

        articleRecently = mListArticle.get(holder.getAdapterPosition());
        Date timeRead = new Date();
        long timestamp = timeRead.getTime();
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("recently_read/"+id_User);
//        databaseReference.child(articleRecently.getId()).child("timestamp").setValue(timestamp);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                 Kiểm tra số lượng phần tử
                if (dataSnapshot.getChildrenCount() >= 6) {
                    // Tạo một danh sách để lưu trữ các phần tử
                    List<DataSnapshot> snapshotList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshotList.add(snapshot);
                    }

                    // Sắp xếp danh sách theo timestamp tăng dần
                    Collections.sort(snapshotList, new Comparator<DataSnapshot>() {
                        @Override
                        public int compare(DataSnapshot snapshot1, DataSnapshot snapshot2) {
                            long timestamp1 = snapshot1.getValue(Long.class);
                            long timestamp2 = snapshot2.getValue(Long.class);
                            return Long.compare(timestamp1, timestamp2);
                        }
                    });

                    // Xóa phần tử có timestamp ít nhất
                    String firstKey = snapshotList.get(0).getKey();
                    databaseReference.child(firstKey).removeValue();
                }

                // Thêm mới phần tử hiện tại
                databaseReference.child(articleRecently.getId()).setValue(timestamp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xảy ra lỗi khi truy cập Firebase
            }
        });
//        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("users/"+id_User+"/recently_read");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Lấy dữ liệu hiện có từ Firebase
//                ArrayList<Article> recently_read = (ArrayList<Article>) dataSnapshot.getValue();
//
//                if (recently_read == null) {
//                    recently_read = new ArrayList<>();
//                }else if (recently_read.size() >= 6) {
//                    // Xóa bài báo đầu tiên
//                    recently_read.remove(0);
//                }
//                // Thêm article mới vào recently_read
//                recently_read.add(articleRecently);
//
//                // Đẩy recently_read lên Firebase
//                databaseReference.setValue(recently_read)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Đã đẩy thành công Map vào database
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Xảy ra lỗi khi đẩy Map vào database
//                            }
//                        });
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xảy ra lỗi khi truy cập Firebase
//            }
//        });
    }
}
