package com.example.appdocbao.Model;

import androidx.annotation.NonNull;

public class Article {
     private String id;
     private String author;
     private int categoryId;
     private String content;
     private String title;
     private String img;
     private long timestamp;
     //https://www.javatpoint.com/java-date-to-timestamp

     public Article() {
     }

     public Article(String id, String title, String content, int categoryId, String author, String img, long timestamp) {
          this.id = id;
          this.author = author;
          this.categoryId = categoryId;
          this.content = content;
          this.title = title;
          this.img = img;
          this.timestamp = timestamp;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getAuthor() {
          return author;
     }

     public void setAuthor(String author) {
          this.author = author;
     }

     public int getCategoryId() {
          return categoryId;
     }

     public void setCategoryId(int categoryId) {
          this.categoryId = categoryId;
     }

     public String getContent() {
          return content;
     }

     public void setContent(String content) {
          this.content = content;
     }

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public long getTimestamp() {
          return timestamp;
     }

     public void setTimestamp(long timestamp) {
          this.timestamp = timestamp;
     }

     public String getImg() {
          return img;
     }

     public void setImg(String img) {
          this.img = img;
     }

     @NonNull
     @Override
     public String toString() {
          return "Article{" +
                  "title: " + title +
                  "catrgory: " + categoryId + "}";
     }
}
