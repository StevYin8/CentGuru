package com.cs673.myapplication.bean;

import android.graphics.Bitmap;

public class PopUpImageCardModel {

    //Attributes
    private int image;

    //Constructor
    public PopUpImageCardModel( int image){
        this.image = image;
    }

    //Getters and Setters
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
