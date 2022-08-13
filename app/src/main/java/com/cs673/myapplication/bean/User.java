package com.cs673.myapplication.bean;

public class User {
    private int id;
    private String name;  
    private String email;
    private String password;
    private String questionOne;
    private String questionTwo;
    private String questionThree;
    private int showInactiveGoal;//0: don't show , 1: show

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestionOne() {
        return questionOne;
    }

    public void setQuestionOne(String questionOne) {
        this.questionOne = questionOne;
    }

    public String getQuestionTwo() {
        return questionTwo;
    }

    public void setQuestionTwo(String questionTwo) {
        this.questionTwo = questionTwo;
    }

    public String getQuestionThree() {
        return questionThree;
    }

    public void setQuestionThree(String questionThree) {
        this.questionThree = questionThree;
    }

    public int getShowInactiveGoal() {
        return showInactiveGoal;
    }

    public void setShowInactiveGoal(int showInactiveGoal) {
        this.showInactiveGoal = showInactiveGoal;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", questionOne='" + questionOne + '\'' +
                ", questionTwo='" + questionTwo + '\'' +
                ", questionThree='" + questionThree + '\'' +
                '}';
    }
}
