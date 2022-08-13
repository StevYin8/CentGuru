package com.cs673.myapplication.bean;

public class BadgeCardModel {

    //Attributes
    private String name;
    private int img;
    private boolean isEarned;

    //Constructor

    public BadgeCardModel(String name, int img, boolean isEarned) {
        this.name = name;
        this.img = img;
        this.isEarned = isEarned;
    }


    //Get and Set



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isEarned() {
        return isEarned;
    }

    public void setEarned(boolean earned) {
        isEarned = earned;
    }
}
