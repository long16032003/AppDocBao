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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecentlyReadAdapter extends RecyclerView.Adapter<RecentlyReadAdapter.RecentlyViewHolder>{
    private Context mContext;
    private ArrayList<Article> mListArticle;
    public RecentlyReadAdapter(Context mContext, ArrayList<Article> mListArticle) {
        this.mContext = mContext;
        this.mListArticle = mListArticle;
    }
    public void setData(ArrayList<Article> list){
        this.mListArticle = list;
        notifyDataSetChanged();     // thực hiện bind load data vào adapter
    }
    @NonNull
    @Override
    public RecentlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View articleView = inflater.inflate(R.layout.item_recentlyread, parent, false);
        RecentlyViewHolder viewHolder = new RecentlyViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewHolder holder, int position) {
        Article article = mListArticle.get(position);
        if(article == null){
            return;
        }
        holder.titleArticleRecently.setText(article.getTitle());
        Glide.with(mContext)
                .load(article.getImg())
                .into(holder.imgArticleRecently);
        holder.authorArticleRecently.setText(article.getAuthor());
        long timestamp = article.getTimestamp();
        Date dateTime = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDateTime = dateFormat.format(dateTime);
        holder.dateArticleRecently.setText(formattedDateTime);
        holder.cardViewArticleRecently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                intent.putExtra("Id", mListArticle.get(holder.getAdapterPosition()).getId());
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

    public class RecentlyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgArticleRecently;
        private TextView titleArticleRecently;
        private CardView cardViewArticleRecently;
        private TextView authorArticleRecently;
        private TextView dateArticleRecently;

        public RecentlyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArticleRecently = itemView.findViewById(R.id.imgArticleRecently);
            titleArticleRecently = itemView.findViewById(R.id.titleArticleRecently);
            cardViewArticleRecently = itemView.findViewById(R.id.cardViewArticleRecently);
            authorArticleRecently = itemView.findViewById(R.id.authorArticleRecently);
            dateArticleRecently = itemView.findViewById(R.id.dateArticleRecently);
        }
    }
}
