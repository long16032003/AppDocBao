package com.example.appdocbao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Activity.DetailArticleActivity;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;

import java.util.*;
import java.text.SimpleDateFormat;


public class RecyclerArticleAdapter extends RecyclerView.Adapter<RecyclerArticleAdapter.ArticleViewHolder> {
    private Context mContext;
    private ArrayList<Article> mListArticle;

    public RecyclerArticleAdapter(Context mContext, ArrayList<Article> mListArticle) {
        this.mContext = mContext;
        this.mListArticle = mListArticle;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDateTime = dateFormat.format(dateTime);
        holder.dateArticle.setText(formattedDateTime);
        holder.cardViewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                intent.putExtra("Image", mListArticle.get(holder.getAdapterPosition()).getImg());
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
        return mListArticle.size();
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
}
