package com.example.appdocbao.Model;

import java.sql.Timestamp;
import java.util.Date;

public class Article {
     private String author;
     private int categoryId;
     private String content;
     private String title;
     private int img;
     private Timestamp date;
     //https://www.javatpoint.com/java-date-to-timestamp
     private int likes;
     private int dislikes;

     public Article() {
     }

     public Article(String title, int img) {
          this.title = title;
          this.img = img;
     }
     public Article(String author, int categoryId, String content, String title, int img, Timestamp date, int likes, int dislikes) {
          this.author = author;
          this.categoryId = categoryId;
          this.content = content;
          this.title = title;
          this.img = img;
          this.date = date;
          this.likes = likes;
          this.dislikes = dislikes;
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

     public int getImg() {
          return img;
     }

     public void setImg(int img) {
          this.img = img;
     }

     public Timestamp getDate() {
          return date;
     }

     public void setDate(Timestamp date) {
          this.date = date;
     }

     public int getLikes() {
          return likes;
     }

     public void setLikes(int likes) {
          this.likes = likes;
     }

     public int getDislikes() {
          return dislikes;
     }

     public void setDislikes(int dislikes) {
          this.dislikes = dislikes;
     }
}
