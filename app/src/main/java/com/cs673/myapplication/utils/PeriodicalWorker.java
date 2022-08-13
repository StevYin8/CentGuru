package com.cs673.myapplication.utils;

import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.cs673.myapplication.BudgetEntry;
import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.BudgetCardModel;
import com.cs673.myapplication.dao.BudgetDao;


public class PeriodicalWorker extends Worker {


    public PeriodicalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        BudgetDao dao = new BudgetDao(getApplicationContext());
        int budgetId = getInputData().getInt("budgetId",-1);
        Budget budget = dao.selectBudgetById(budgetId);

        //!CalendarUtil.getTime().equals(budget.getEndDate())
        if (budget.getId()<=0){
            Log.e("Periodical Work", "cancel work: "+budget.getId());
            return Result.failure();
        }else{
            String timeCycle = budget.getTimeCycle();
            String date = budget.getEndDate();
            dao.updateBudgetStatus(budget.getId());//Make old budge status to be 1
            Log.i("BudgetPeriodicalWork", "doWork: "+budget.toString());
            if(timeCycle!=null &&timeCycle.equals("Monthly")){
                String endDate = CalendarUtil.getTargetDate(date,30);
                budget.setEndDate(endDate);
                dao.insertBudget(budget);
                int newBudgetId =  dao.getMaxId();
                WorkUtil.startWork(newBudgetId,30,getApplicationContext());
            }
            if(timeCycle!=null && timeCycle.equals("Weekly")){
                String  endDate = CalendarUtil.getTargetDate(date,7);
                budget.setEndDate(endDate);
                dao.insertBudget(budget);
                int newBudgetId =  dao.getMaxId();
                WorkUtil.startWork(newBudgetId,7,getApplicationContext());
            }
            return Result.success();
        }
    }
}
