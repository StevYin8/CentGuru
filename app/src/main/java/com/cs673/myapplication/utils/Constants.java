package com.cs673.myapplication.utils;

public class Constants {
    public static final String DATABASE_NAME = "DataBase.db";
    public static final int DATABASE_VERSION = 2;

    // User Table
    public static final String USER_TABLE_NAME = "userTable";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_EMAIL_ADDRESS = "email_address";
    public static final String COLUMN_PASSWORD = "password";
    public static final String QUESTION_ONE="questionOne";
    public static final String QUESTION_TWO="questionTwo";
    public static final String QUESTION_Three="questionThree";
    public static final String showInactiveGoal="showInactiveGoal";

   /* private static final String COLUMN_SECURITY_QUESTION = "security_question";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_USER_IMAGE = "userImage";*/


    // transaction table
    public static final String TRANSACTION_TABLE_NAME = "transactionTable";
    public static final String COLUMN_TRANSACTION_ID = "transactionId";
    public static final String COLUMN_AMOUNT= "amount";
    public static final String COLUMN_TYPE= "type";
    public static final String COLUMN_DATE= "date";
    public static final String COLUMN_REFERENCE_BUDGET_ID = "budgetReferenceId";
    public static final String COLUMN_REFERENCE_SAVINGS_ID = "savingsGoalReference";
    public static final String COLUMN_NOTE = "note";
    //public static final String COLUMN_TRANSACTION_IMAGE = "transactionImage";


    // budget table
    public static final String BUDGET_TABLE_NAME = "budgetTable";
    public static final String COLUMN_BUDGET_ID = "budgetId";
    public static final String COLUMN_BUDGET_NAME = "budgetName";
    public static final String COLUMN_TIME_CYCLE= "timeCycle";
    public static final String COLUMN_LIMIT_AMOUNT= "limitAmount";
    public static final String COLUMN_BUDGET_NOTE = "budgetNote";
    public static final String COLUMN_END_DATE="endDate";
    public static final String STATUS = "status";

    //Savings Table
    public static final String SAVINGS_TABLE_NAME = "savingsTable";
    public static final String COLUMN_SAVINGS_ID = "savingsId";
    public static final String COLUMN_SAVINGS_NAME = "savingsName";
    public static final String COLUMN_TARGET_AMOUNT="targetAmount";
    public static final String COLUMN_TARGET_DATE="targetDate";
    public static final String COLUMN_SAVINGS_NOTE = "savingsNote";
    public static final String COLUMN_SAVINGS_STATUS = "status";
    //public static final String COLUMN_SAVINGS_IMAGE = "transactionTable";

    public static final String IMG_CAT_TABLE_NAME = "ImgCategoryTable";
    public static final String CAT = "Category";
    public static final String IMG = "Img";

    //TAG for error
    public static final String TAG = "error";

    public static final String userId = "userId";

    //Default image names to be inserted into the database
    public static final String []goalImgNames = {"electronics","fitness","daily","medical","travel","car","shoes","amusement","goal"};
    public static final String []budgetImgNames = {"art","clothes","entertainment","gas","food","pets","transportation","shopping","budget"};
    public static final String []defaultImgNames={"others"};
}
