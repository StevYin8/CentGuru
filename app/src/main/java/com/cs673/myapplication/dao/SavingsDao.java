package com.cs673.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.cs673.myapplication.bean.GoalCardModel;
import com.cs673.myapplication.bean.GoalDropDownModel;
import com.cs673.myapplication.bean.Savings;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.DBHelper;

import java.util.ArrayList;

public class SavingsDao {

    private DBHelper helper;
    private ImgDao imgDao;
    private Context context;
    public SavingsDao(Context context){
        helper =  DBHelper.getInstance(context);
        imgDao = new ImgDao(context);
        this.context = context;
    }

    public long insertGoal(Savings saving) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_SAVINGS_NAME,saving.getName());
        values.put(Constants.CAT,saving.getCategory());
        values.put(Constants.COLUMN_TARGET_AMOUNT,saving.getTargetAmount());
        values.put(Constants.COLUMN_TARGET_DATE,saving.getTargetDate());
        values.put(Constants.COLUMN_SAVINGS_NOTE,saving.getNote());
        values.put(Constants.COLUMN_SAVINGS_STATUS,saving.getStatus());
        values.put(Constants.userId,saving.getUserId());
        long result = db.insert(Constants.SAVINGS_TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public ArrayList<GoalCardModel> selectAll(int userId){
        ArrayList<GoalCardModel> goals = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Constants.SAVINGS_TABLE_NAME+" where status=1 AND userId=?"
                ,new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            GoalCardModel goal = new GoalCardModel();
            int id = cursor.getInt(0);
            goal.setId(id);
            String category = cursor.getString(1);
            String name = cursor.getString(2);
            goal.setGoalName(name);
            int image = imgDao.getImgByCategory(category);
            goal.setImage(image);
            TransactionDao transactionDao = new TransactionDao(context);
            String currentAmount = transactionDao.getGoalAmount(id);

            goal.setCurrentAmt(currentAmount);

            String targetAmount =cursor.getString(3);

            goal.setTargetAmt(targetAmount);

            int progress = (int) (Float.parseFloat(currentAmount)/Float.parseFloat(targetAmount)*100);
            goal.setProgress(progress);

            String targetDate = cursor.getString(4);
            goal.setTargetDate(targetDate);
            goals.add(goal);
        }
        cursor.close();
        db.close();
        return goals;
    }

    public ArrayList<GoalCardModel> selectAllGoals(int userId){
        ArrayList<GoalCardModel> goals = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Constants.SAVINGS_TABLE_NAME+" where userId=?"
                ,new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            GoalCardModel goal = new GoalCardModel();
            int id = cursor.getInt(0);
            goal.setId(id);
            String category = cursor.getString(1);
            String name = cursor.getString(2);
            goal.setGoalName(name);
            int image = imgDao.getImgByCategory(category);
            goal.setImage(image);
            TransactionDao transactionDao = new TransactionDao(context);
            String currentAmount = transactionDao.getGoalAmount(id);

            goal.setCurrentAmt(currentAmount);

            String targetAmount =cursor.getString(3);

            goal.setTargetAmt(targetAmount);

            int progress = (int) (Float.parseFloat(currentAmount)/Float.parseFloat(targetAmount)*100);
            goal.setProgress(progress);

            String targetDate = cursor.getString(4);
            goal.setTargetDate(targetDate);
            goals.add(goal);
        }
        cursor.close();
        db.close();
        return goals;
    }

    public ArrayList<GoalDropDownModel> selectGoalNames(int userId){
        ArrayList<GoalDropDownModel> goals = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select savingsId,savingsName,Category from "+Constants.SAVINGS_TABLE_NAME+" where status=1 AND userId=?"
                                    ,new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            GoalDropDownModel goal = new GoalDropDownModel();
            int goalId = cursor.getInt(0);
            String goalName = cursor.getString(1);
            String category = cursor.getString(2);
            goal.setId(goalId);
            goal.setGoalName(goalName);
            goal.setCategory(category);
            goals.add(goal);
        }
        cursor.close();
        db.close();
        return goals;
    }


    public int getCount(int goalId,int userId){
        int number = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from "+Constants.SAVINGS_TABLE_NAME+" where status=1 AND "+
                                        "savingsId<=? AND userId=?",new String[]{Integer.toString(goalId),Integer.toString(userId)});
        while (cursor.moveToNext()){
            number = cursor.getInt(0);
        }
        return number;
    }

    public Savings selectGoalById(int goalId){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Constants.SAVINGS_TABLE_NAME + " where " + Constants.COLUMN_SAVINGS_ID + "=?"
                , new String[]{Integer.toString(goalId)});
        Savings goal = new Savings();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String name = cursor.getString(2);
            String targetAmount = cursor.getString(3);
            String date = cursor.getString(4);
            String note = cursor.getString(5);
            int status = cursor.getInt(6);
            goal.setId(id);
            goal.setCategory(category);
            goal.setName(name);
            goal.setTargetAmount(targetAmount);
            goal.setTargetDate(date);
            goal.setNote(note);
            goal.setStatus(status);
        }
        cursor.close();
        db.close();
        return goal;
    }

    public void editGoal(Savings goal){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Category",goal.getCategory());
        values.put("savingsName",goal.getName());
        values.put("targetAmount",goal.getTargetAmount());
        values.put("targetDate",goal.getTargetDate());
        values.put("savingsNote",goal.getNote());
        values.put("status",goal.getStatus());

        db.update(Constants.SAVINGS_TABLE_NAME,values,"savingsId=?",new String[]{Integer.toString(goal.getId())});
        db.close();
    }

    public void deleteGoalById(int goalId){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(Constants.SAVINGS_TABLE_NAME,"savingsId=?",new String[]{Integer.toString(goalId)});
        db.close();
    }

    public int getGoals(int userId){
        int number = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from "+Constants.SAVINGS_TABLE_NAME+" where userId=?"
                ,new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            number = cursor.getInt(0);
        }
        return number;
    }
}
