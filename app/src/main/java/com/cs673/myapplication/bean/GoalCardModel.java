package com.cs673.myapplication.bean;


public class GoalCardModel {

    //Attributes
    private int id;
    private String goalName;
    private String currentAmt;
    private String targetAmt;
    private String targetDate;
    private int progress;
    private int image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Getters and Setters
    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getCurrentAmt() {
        return currentAmt;
    }

    public void setCurrentAmt(String currentAmt) {
        this.currentAmt = currentAmt;
    }

    public String getTargetAmt() {
        return targetAmt;
    }

    public void setTargetAmt(String targetAmt) {
        this.targetAmt = targetAmt;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }
}
