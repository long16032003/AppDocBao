package com.example.appdocbao.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String name;
    private String email;
    private int points;

    private String phone;
    private String img;
    private Map<String,Article> saved_articles;
    private Map<String,Article> recently_read;
    private Map<String,Article> user_articles;
    public User() {
    }

    public User(String id, String name, String email, int points, String phone, String img, Map<String,Article> saved_articles, Map<String,Article> recently_read, Map<String,Article> user_articles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.points = points;
        this.phone = phone;
        this.img = img;
        this.saved_articles = saved_articles;
        this.recently_read = recently_read;
        this.user_articles = user_articles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Map<String,Article> getSaved_articles() {
        return saved_articles;
    }

    public void setSaved_articles(Map<String,Article> saved_articles) {
        this.saved_articles = saved_articles;
    }

    public Map<String,Article> getRecently_read() {
        return recently_read;
    }

    public void setRecently_read(Map<String,Article> recently_read) {
        this.recently_read = recently_read;
    }

    public Map<String,Article> getUser_articles() {
        return user_articles;
    }

    public void setUser_articles(Map<String,Article> user_articles) {
        this.user_articles = user_articles;
    }
}
