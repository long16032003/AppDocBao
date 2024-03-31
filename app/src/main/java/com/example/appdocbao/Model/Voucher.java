package com.example.appdocbao.Model;

public class Voucher {

    private String id;
    private String title;
    private String img;
    private long expiryTimestamp;
    private int achievePoints;

    public Voucher() {
    }

    public Voucher(String id, String title, String img, long expiryTimestamp, int achievePoints) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.expiryTimestamp = expiryTimestamp;
        this.achievePoints = achievePoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public int getAchievePoints() {
        return achievePoints;
    }

    public void setAchievePoints(int achievePoints) {
        this.achievePoints = achievePoints;
    }
}