package com.cs673.myapplication.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.Savings;
import com.cs673.myapplication.bean.Transaction;
import com.cs673.myapplication.bean.TransactionCardModel;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.DBHelper;

import java.util.ArrayList;

public class TransactionDao {

    private DBHelper helper;
    private ImgDao imgDao;
    private Context context;
    public TransactionDao(Context context){
        this.context = context;
        helper =  DBHelper.getInstance(context);
        imgDao = new ImgDao(context);
    }

    public long insertTransaction(Transaction transaction){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_AMOUNT,transaction.getAmount());
        values.put(Constants.COLUMN_TYPE,transaction.getType());
        values.put(Constants.COLUMN_DATE,transaction.getDate());
        values.put(Constants.COLUMN_REFERENCE_BUDGET_ID ,transaction.getBudgetId());
        values.put(Constants.COLUMN_REFERENCE_SAVINGS_ID,transaction.getSavingsId());
        values.put(Constants.COLUMN_NOTE,transaction.getNote());
        values.put(Constants.userId,transaction.getUserId());

        long result = db.insert(Constants.TRANSACTION_TABLE_NAME,null,values);
        db.close();
        return result;
    }

    public ArrayList<TransactionCardModel> selectAll(int userId){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Constants.TRANSACTION_TABLE_NAME+" where userId=?",new String[]{Integer.toString(userId)});
        ArrayList<TransactionCardModel> transactions = new ArrayList<>();
        while (cursor.moveToNext()){
            TransactionCardModel transaction = new TransactionCardModel();
            int id = cursor.getInt(0);
            transaction.setId(id);
            float amount =  cursor.getFloat(1);
            transaction.setAmount(amount);
            int type = cursor.getInt(2);
            String date = cursor.getString(3);
            transaction.setDate(date);
            int budgetId = cursor.getInt(4);
            int goalId = cursor.getInt(5);
            ArrayList<String> result = getCatAndName(goalId,budgetId,type);
            String category = result.get(0);
            int img= imgDao.getImgByCategory(category);
            transaction.setImage(img);
            String name = result.get(1);
            transaction.setName(name);

            transactions.add(transaction);
        }
        cursor.close();
        db.close();
        return transactions;
    }

    public Transaction selectById(int id,int userId){
        SQLiteDatabase db = helper.getReadableDatabase();
        Transaction transaction = new Transaction();
        Cursor cursor = db.rawQuery("select * from "+Constants.TRANSACTION_TABLE_NAME+" where transactionId=? AND userId=?"
                        ,new String[]{Integer.toString(id),Integer.toString(userId)});
        while (cursor.moveToNext()){
            transaction.setId(id);
            Float amount = cursor.getFloat(1);
            transaction.setAmount(amount);
            int type = cursor.getInt(2);
            transaction.setType(type);
            String date = cursor.getString(3);
            transaction.setDate(date);
            int budgetId = cursor.getInt(4);
            transaction.setBudgetId(budgetId);
            int goalId = cursor.getInt(5);
            transaction.setSavingsId(goalId);
            String note = cursor.getString(6);
            transaction.setNote(note);

        }
        return transaction;
    }

    public void updateTransaction(Transaction transaction){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_AMOUNT,transaction.getAmount());
        values.put(Constants.COLUMN_TYPE,transaction.getType());
        values.put(Constants.COLUMN_DATE,transaction.getDate());
        values.put(Constants.COLUMN_REFERENCE_BUDGET_ID ,transaction.getBudgetId());
        values.put(Constants.COLUMN_REFERENCE_SAVINGS_ID,transaction.getSavingsId());
        values.put(Constants.COLUMN_NOTE,transaction.getNote());
        db.update(Constants.TRANSACTION_TABLE_NAME,values,"transactionId=?"
                            ,new String[]{Integer.toString(transaction.getId())});
        db.close();
    }

    public void deleteById(int transactionId){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(Constants.TRANSACTION_TABLE_NAME, "transactionId=?", new String[]{Integer.toString(transactionId)});
        db.close();
    }

   public float getBudgetAmount(int budgetId){
        SQLiteDatabase db = helper.getReadableDatabase();
        float amount = 0;
        Cursor cursor = db.rawQuery("select amount,type from " + Constants.TRANSACTION_TABLE_NAME + " where budgetReferenceId=?"
               , new String[]{Integer.toString(budgetId)});
         while (cursor.moveToNext()){
             float currentAmount = cursor.getFloat(0);
             int type = cursor.getInt(1);
             //0:spending 1:saving
             if(type==1){
                 amount -=currentAmount;
             }else{
                 amount +=currentAmount;
             }
         }
         cursor.close();
         return amount;

   }

   public String getGoalAmount(int goalId){
        SQLiteDatabase db = helper.getReadableDatabase();
        float amount = 0l;
        Cursor cursor = db.rawQuery("select amount,type from " + Constants.TRANSACTION_TABLE_NAME + " where savingsGoalReference=?"
               , new String[]{Integer.toString(goalId)});
        while (cursor.moveToNext()){
           float currentAmount = cursor.getFloat(0);
           int type = cursor.getInt(1);
           //0:spending 1:saving
           if(type==0){
               amount -=currentAmount;
           }else{
               amount +=currentAmount;
           }
        }
        cursor.close();
        return Float.toString(amount);
   }

   public ArrayList<String> getCatAndName(int goalId,int budgetId,int type){
        ArrayList<String> result = new ArrayList<>();
        if(goalId==0 && budgetId==0){
            result.add("others");
            result.add("others");
            return result;
        }
       //0:spending 1:saving
       if(goalId!=0){
           if(type==1 || budgetId==0){
               SavingsDao goalDao = new SavingsDao(context);
               Savings goal = goalDao.selectGoalById(goalId);
               result.add(goal.getCategory());
               result.add(goal.getName());
               return result;
           }
       }

       BudgetDao budgetDao = new BudgetDao(context);
       Budget budget = budgetDao.selectBudgetById(budgetId);
       result.add(budget.getCategory());
       result.add(budget.getName());
       return result;

   }

}

