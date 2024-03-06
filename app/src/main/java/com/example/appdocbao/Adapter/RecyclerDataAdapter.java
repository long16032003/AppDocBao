package com.example.appdocbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;

import java.util.ArrayList;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ArticleViewHolder> {
    private Context mContext;
    private ArrayList<Article> mListArticle;

    public RecyclerDataAdapter(Context mContext, ArrayList<Article> mListArticle) {
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
        holder.imgArticle.setImageResource(article.getImg());
        holder.titleArticle.setText(article.getTitle());
//        holder.authorArticle.setText(article.getAuthor());
//        holder.dateArticle.setText(article.getDate());
    }

    @Override
    public int getItemCount() {
        return mListArticle.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgArticle;
        private TextView titleArticle;
//        private TextView authorArticle;
//        private TextView dateArticle;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArticle = itemView.findViewById(R.id.imgArticle);
            titleArticle = itemView.findViewById(R.id.titleArticle);
//            authorArticle = itemView.findViewById(R.id.authorArticle);
//            dateArticle = itemView.findViewById(R.id.dateArticle);
        }
    }
}
