package com.cs673.myapplication.bean;


public class TransactionCardModel {

    private int id;
    private int image;
    private String name;
    private String date;
    private float amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionCardModel{" +
                "id=" + id +
                ", image=" + image +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }
}
