package com.cs673.myapplication.bean;

public class Budget {
    private int id;
    private String category;
    private String name;
    private String timeCycle;
    private float limitAmount;
    private String note;
    private String endDate;
    private int status ; //0:in use ,1: history
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(String timeCycle) {
        this.timeCycle = timeCycle;
    }


    public float getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(float limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", timeCycle='" + timeCycle + '\'' +
                ", limitAmount=" + limitAmount +
                ", note='" + note + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                '}';
    }
}
