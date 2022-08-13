package com.cs673.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.BudgetCardModel;
import com.cs673.myapplication.bean.BudgetDropDownModel;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.DBHelper;

import java.util.ArrayList;

public class BudgetDao {

    private Context context;
    private DBHelper dbHelper;
    public BudgetDao(Context context){
        dbHelper = DBHelper.getInstance(context);
        this.context=context;
    }

    public long insertBudget(Budget budget){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_BUDGET_NAME,budget.getName());
        values.put(Constants.COLUMN_TIME_CYCLE,budget.getTimeCycle());
        values.put(Constants.CAT,budget.getCategory());
        values.put(Constants.COLUMN_LIMIT_AMOUNT,budget.getLimitAmount());
        values.put(Constants.COLUMN_END_DATE,budget.getEndDate());
        values.put(Constants.COLUMN_BUDGET_NOTE,budget.getNote());
        values.put(Constants.userId,budget.getUserId());
        values.put(Constants.STATUS,budget.getStatus());//0:in use ,1: history
        long result = db.insert(Constants.BUDGET_TABLE_NAME, null, values);
        db.close();
        return  result;
    }

    public ArrayList<BudgetCardModel> selectAll(int userId){
        ArrayList<BudgetCardModel> budgets = new ArrayList<>();
        TransactionDao transactionDao = new TransactionDao(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + Constants.BUDGET_TABLE_NAME+" where status=0 AND userId=?", new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            BudgetCardModel budget = new BudgetCardModel();
            ImgDao imgDao = new ImgDao(context);
            int id = cursor.getInt(0);
            budget.setId(id);
            String category = cursor.getString(1);
            int img = imgDao.getImgByCategory(category);
            budget.setImg(img);
            String name = cursor.getString(2);
            budget.setName(name);
            String timeCycle = cursor.getString(3);
            budget.setTimeCycle(timeCycle);

            float currentAmount = transactionDao.getBudgetAmount(id);

            budget.setCurrentAmount(currentAmount);

            float limitAmount = cursor.getFloat(4);
            budget.setLimitAmount(limitAmount);
            String endDate = cursor.getString(6);
            budget.setEndDate(endDate);
            budgets.add(budget);
        }
        cursor.close();
        db.close();
        return budgets;
    }

    public ArrayList<BudgetDropDownModel> selectBudgetNames(int userId){
        ArrayList<BudgetDropDownModel> budgets = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select budgetId,Category,budgetName from "+Constants.BUDGET_TABLE_NAME+" where status=0 AND userId=?"
                ,new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            BudgetDropDownModel budget = new BudgetDropDownModel();
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String name = cursor.getString(2);
            budget.setId(id);
            budget.setCategory(category);
            budget.setName(name);
            budgets.add(budget);
        }
        cursor.close();
        db.close();
        return budgets;
    }


    public Budget selectBudgetById(int budgetId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Constants.BUDGET_TABLE_NAME + " where " + Constants.COLUMN_BUDGET_ID + "=?"
                , new String[]{Integer.toString(budgetId)});
        Budget budget = new Budget();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            budget.setId(id);
            String category = cursor.getString(1);
            budget.setCategory(category);
            String name = cursor.getString(2);
            budget.setName(name);
            String timeCycle = cursor.getString(3);
            budget.setTimeCycle(timeCycle);

            float limitAmount = cursor.getFloat(4);
            budget.setLimitAmount(limitAmount);
            String note = cursor.getString(5);
            budget.setNote(note);
            String date = cursor.getString(6);
            budget.setEndDate(date);
            int status = cursor.getInt(7);
            budget.setStatus(status);
            int userId = cursor.getInt(8);
            budget.setUserId(userId);
        }
        cursor.close();
        return budget;
    }


    public int getMaxId(){
        int id = -1;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(budgetId) from " + Constants.BUDGET_TABLE_NAME, null);
        while (cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        db.close();
        return id;
    }

    public int getCount(int budgetId,int userId){
        int number = -1;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from "+Constants.BUDGET_TABLE_NAME+" where status=0 AND budgetId<=? AND userId=?"
                                    ,new String[]{Integer.toString(budgetId),Integer.toString(userId)});
        while (cursor.moveToNext()){
            number = cursor.getInt(0);
        }
        return number;
    }

    public void deleteById(int budgetId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Constants.BUDGET_TABLE_NAME,"budgetId=?",new String[]{Integer.toString(budgetId)});
        db.close();
    }

    public void updateBudget(Budget budget){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Category",budget.getCategory());
        values.put("budgetName",budget.getName());
        values.put("timeCycle",budget.getTimeCycle());
        values.put("limitAmount",budget.getLimitAmount());
        values.put("endDate",budget.getEndDate());
        db.update(Constants.BUDGET_TABLE_NAME,values,"budgetId=?",new String[]{Integer.toString(budget.getId())});
    }

    public void updateBudgetStatus(int budgetId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status",1);
        db.update(Constants.BUDGET_TABLE_NAME,values,"budgetId=?",new String[]{Integer.toString(budgetId)});

    }

    public int getBudgets(int userId){
        int number = -1;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from "+Constants.BUDGET_TABLE_NAME+" where userId=?"
                ,new String[]{Integer.toString(userId)});
        while (cursor.moveToNext()){
            number = cursor.getInt(0);
        }
        return number;
    }
}
