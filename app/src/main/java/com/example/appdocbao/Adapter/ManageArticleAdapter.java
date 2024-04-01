package com.example.appdocbao.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Activity.DetailArticleActivity;
import com.example.appdocbao.Activity.LoginActivity;
import com.example.appdocbao.Activity.UpdateArticleActivity;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ManageArticleAdapter extends RecyclerView.Adapter<ManageArticleAdapter.ManageArticleViewHolder>{
    private Context mContext;
    private ArrayList<Article> mListArticle;


    public ManageArticleAdapter(Context mContext, ArrayList<Article> mListArticle) {
        this.mContext = mContext;
        this.mListArticle = mListArticle;
    }

    @NonNull
    @Override
    public ManageArticleAdapter.ManageArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View articleView = inflater.inflate(R.layout.item_manage_article, parent, false);
        ManageArticleViewHolder viewHolder = new ManageArticleViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageArticleAdapter.ManageArticleViewHolder holder, int position) {
        Article article = mListArticle.get(position);
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String id_User = mAuth.getUid();
        if(article == null){
            return;
        }
        holder.titleArticle.setText(article.getTitle());
        Glide.with(mContext)
                .load(article.getImg())
                .into(holder.imgArticle);
        holder.cardViewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        holder.deleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác thực");
                builder.setMessage("Bạn có chắc muốn xóa bài báo này ?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DatabaseReference savedArticleReference = FirebaseDatabase.getInstance().getReference("saved_articles/"+id_User);
                        DatabaseReference articleRef = FirebaseDatabase.getInstance().getReference("articles").child(article.getId());
                        articleRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
//                                    savedArticleReference.child(article.getId()).removeValue(); // Xóa cả trong mục tin đã lưu
                                    Toast.makeText(v.getContext(), "Xóa bài báo thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.updateArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(mContext, UpdateArticleActivity.class);
                intentUpdate.putExtra("Id", mListArticle.get(holder.getAdapterPosition()).getId());
                intentUpdate.putExtra("Image", mListArticle.get(holder.getAdapterPosition()).getImg());
                intentUpdate.putExtra("idCategory", mListArticle.get(holder.getAdapterPosition()).getCategoryId());
                intentUpdate.putExtra("Title", mListArticle.get(holder.getAdapterPosition()).getTitle());
                intentUpdate.putExtra("Content", mListArticle.get(holder.getAdapterPosition()).getContent());
                intentUpdate.putExtra("Author", mListArticle.get(holder.getAdapterPosition()).getAuthor());
                mContext.startActivity(intentUpdate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListArticle.size();
    }

    public class ManageArticleViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgArticle, updateArticle, deleteArticle;
        private TextView titleArticle;
        private CardView cardViewArticle;

        public ManageArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArticle = itemView.findViewById(R.id.imgArticle);
            titleArticle = itemView.findViewById(R.id.titleArticle);
            cardViewArticle = itemView.findViewById(R.id.cardViewArticle);
            updateArticle = itemView.findViewById(R.id.updateArticle);
            deleteArticle = itemView.findViewById(R.id.deleteArticle);
        }
    }
}
