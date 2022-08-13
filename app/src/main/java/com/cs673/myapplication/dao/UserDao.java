package com.cs673.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cs673.myapplication.R;
import com.cs673.myapplication.bean.BadgeCardModel;
import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.Savings;
import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.DBHelper;

import java.util.ArrayList;


public class UserDao {

    private DBHelper helper;
    private Context context;
    public UserDao(Context context){
        helper =  DBHelper.getInstance(context);
        this.context = context;
    }

    public long insert(User user){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name",user.getName());
        values.put("email_address",user.getEmail());
        values.put("password",user.getPassword());
        values.put("questionOne",user.getQuestionOne());
        values.put("questionTwo",user.getQuestionTwo());
        values.put("questionThree",user.getQuestionThree());
        values.put("showInactiveGoal",user.getShowInactiveGoal());
        long rowId = db.insert("userTable", null, values);
        db.close();
        return rowId;
    }

    public User queryByUserName(String userName){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from userTable where user_name=?";
        String []args = new String[]{userName};
        Cursor cursor = db.rawQuery(sql,args);
        User user = new User();
        while (cursor.moveToNext()){
            int userId = cursor.getInt(0);
            user.setId(userId);
            String name = cursor.getString(1);
            user.setName(name);
            String email = cursor.getString(2);
            user.setEmail(email);
            String password = cursor.getString(3);
            user.setPassword(password);
            String q1 = cursor.getString(4);
            user.setQuestionOne(q1);
            String q2 = cursor.getString(5);
            user.setQuestionTwo(q2);
            String q3 = cursor.getString(6);
            user.setQuestionThree(q3);
            int showInactive = cursor.getInt(7);
            user.setShowInactiveGoal(showInactive);

        }
        cursor.close();
        db.close();
        return user;
    }

    public User queryByUserId(int userId){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from userTable where userId=?";
        String []args = new String[]{Integer.toString(userId)};
        Cursor cursor = db.rawQuery(sql,args);
        User user = new User();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            user.setId(id);
            String name = cursor.getString(1);
            user.setName(name);
            String email = cursor.getString(2);
            user.setEmail(email);
            String password = cursor.getString(3);
            user.setPassword(password);
            String q1 = cursor.getString(4);
            String q2 = cursor.getString(5);
            String q3 = cursor.getString(6);
            user.setQuestionOne(q1);
            user.setQuestionTwo(q2);
            user.setQuestionThree(q3);
            int showInactive = cursor.getInt(7);
            user.setShowInactiveGoal(showInactive);

        }
        cursor.close();
        db.close();
        return user;
    }

    public void updateUser(User user){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("user_name",user.getName());
        values.put("email_address",user.getEmail());
        values.put("password",user.getPassword());
        values.put("questionOne",user.getQuestionOne());
        values.put("questionTwo",user.getQuestionTwo());
        values.put("questionThree",user.getQuestionThree());
        db.update(Constants.USER_TABLE_NAME,values,"userId=?",new String[]{Integer.toString(user.getId())});
        db.close();
    }

    public void updateStatus(int status,int userId){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("showInactiveGoal",status);
        db.update(Constants.USER_TABLE_NAME,values,"userId=?",new String[]{Integer.toString(userId)});

    }

    public ArrayList<BadgeCardModel> getBadges(int userId){
        ArrayList<BadgeCardModel> badgeCardModels = new ArrayList<>();
        BudgetDao budgetDao = new BudgetDao(context);
        SavingsDao goalDao = new SavingsDao(context);
        int budgetCount = budgetDao.getBudgets(userId);
        int goalCount = goalDao.getGoals(userId);

        if(budgetCount<1){
            badgeCardModels.add(new BadgeCardModel("1st budget", R.drawable.new_1st_budget,false));
        }else {
            badgeCardModels.add(new BadgeCardModel("1st budget", R.drawable.new_1st_budget,true));
        }

        if(budgetCount<5){
            badgeCardModels.add(new BadgeCardModel("5th budget", R.drawable.new_5th_budget,false));
        }else {
            badgeCardModels.add(new BadgeCardModel("5th budget", R.drawable.new_5th_budget,true));
        }

        if(budgetCount<10){
            badgeCardModels.add(new BadgeCardModel("10th budget", R.drawable.new_10th_budget,false));
        }else {
            badgeCardModels.add(new BadgeCardModel("10th budget", R.drawable.new_10th_budget,true));
        }



        if (goalCount<1){
            badgeCardModels.add(new BadgeCardModel("1st goal",R.drawable.new_1st_goal,false));
        }else {
            badgeCardModels.add(new BadgeCardModel("1st goal",R.drawable.new_1st_goal,true));
        }

        if (goalCount<5){
            badgeCardModels.add(new BadgeCardModel("5th goal",R.drawable.new_5th_goal,false));
        } else {
            badgeCardModels.add(new BadgeCardModel("5th goal",R.drawable.new_5th_goal,true));
        }

        if(goalCount<10){
            badgeCardModels.add(new BadgeCardModel("10th goal",R.drawable.new_10th_goal,false));
        }else {
            badgeCardModels.add(new BadgeCardModel("10th goal", R.drawable.new_10th_goal, true));
        }

        return badgeCardModels;
    }

    public ArrayList<BadgeCardModel> getActiveBadges(int userId) {
        ArrayList<BadgeCardModel> badgeCardModels = new ArrayList<>();
        BudgetDao budgetDao = new BudgetDao(context);
        SavingsDao goalDao = new SavingsDao(context);
        int budgetCount = budgetDao.getBudgets(userId);
        int goalCount = goalDao.getGoals(userId);
        if (budgetCount>=1){
            badgeCardModels.add(new BadgeCardModel("1st budget", R.drawable.new_1st_budget,true));
        }
        if (budgetCount>=5){
            badgeCardModels.add(new BadgeCardModel("5th budget", R.drawable.new_5th_budget,true));
        }
        if (budgetCount>=10){
            badgeCardModels.add(new BadgeCardModel("10th budget", R.drawable.new_10th_budget,true));
        }

        if(goalCount>=1){
            badgeCardModels.add(new BadgeCardModel("1st goal", R.drawable.new_1st_goal,true));
        }

        if(goalCount>=5){
            badgeCardModels.add(new BadgeCardModel("5th goal", R.drawable.new_5th_goal,true));
        }
        if(goalCount>=10){
            badgeCardModels.add(new BadgeCardModel("10th goal", R.drawable.new_10th_goal,true));
        }
        return badgeCardModels;
    }

}
