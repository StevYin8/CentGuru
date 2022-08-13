package com.cs673.myapplication.utils;

import android.content.Context;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WorkUtil {

    public static void startWork(int budgetId, int days, Context context){
        Data data = new Data.Builder().putInt("budgetId",budgetId).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PeriodicalWorker.class)
                .setInitialDelay(days, TimeUnit.MINUTES)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
