package com.cs673.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.dao.BudgetDao;
import com.cs673.myapplication.dao.ImgDao;
import com.cs673.myapplication.utils.CalendarUtil;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.PeriodicalWorker;
import com.cs673.myapplication.utils.ToastUtil;
import com.cs673.myapplication.utils.WorkUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class BudgetEntry extends AppCompatActivity {

    //Buttons to Save and Discard
    Button btnSave;
    Button btnDelete;
    Button btnImagePopUp;
    ImageView imageView,imgBudgetIcon;
    EditText etBudgetName,etAmount;
    TextView tvCatName;

    RadioGroup radioGroup;
    RadioButton rbMonthly;
    RadioButton rbWeekly;
    int type = 0; //0: Monthly , 1: Weekly
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_entry);
        initView();


        //Set On Click Listener for Save Button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etBudgetName.getText().toString().trim();
                if(name==null||name.length()==0){
                    ToastUtil.toastShort(BudgetEntry.this,"Please input budget name!");
                    return;
                }

                String category = tvCatName.getText().toString().trim();
                if(category==null||category.length()==0){
                    ToastUtil.toastShort(BudgetEntry.this,"Please select a category!");
                    return;
                }

                String amountStr =  etAmount.getText().toString().trim();
                if(amountStr==null||amountStr.length()==0){
                    ToastUtil.toastShort(BudgetEntry.this,"Please input target amount!");
                    return;
                }
                float amount = Float.valueOf(amountStr);


                Budget budget = new Budget();
                if(type==0){
                    budget.setTimeCycle("Monthly");
                    String date = CalendarUtil.getTargetDate(30);
                    budget.setEndDate(date);
                }else {
                    budget.setTimeCycle("Weekly");
                    String date = CalendarUtil.getTargetDate(7);
                    budget.setEndDate(date);
                }

                budget.setName(name);
                budget.setCategory(category);
                budget.setLimitAmount(amount);
                //budget.setNote();
                budget.setUserId(userId);
                budget.setStatus(0);
                BudgetDao dao = new BudgetDao(BudgetEntry.this);
                long result = dao.insertBudget(budget);
                if(result==-1){
                    ToastUtil.toastShort(BudgetEntry.this,"Fail to insert a budge!");
                }else{
                    if(type==0){
                        WorkUtil.startWork(dao.getMaxId(),30,getApplicationContext());
                    }else{
                        WorkUtil.startWork(dao.getMaxId(),7,getApplicationContext());
                    }
                    Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
            }
        });

        //Set OnclickListener for Button to Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        //Set on Click Listener for image pick button
        btnImagePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(v);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_BudgetMonthly:
                        type = 0;
                        break;
                    case R.id.radioButton_BudgetWeekly:
                        type = 1;
                        break;
                    default:
                        Log.i(Constants.TAG,"Radio group error!");
                        break;
                }
            }
        });


    }

    private void initView() {
        //Set up view elements
        btnSave = findViewById(R.id.btn_BudgetSave);
        btnDelete = findViewById(R.id.btn_BudgetDelete);
        btnImagePopUp = findViewById(R.id.btn_BudgetImagePick);
        etBudgetName = findViewById(R.id.eTxt_BudgetName);
        imgBudgetIcon = findViewById(R.id.img_BudgetEntryIcon);
        imageView = findViewById(R.id.img_BudgetEntryIcon);
        etAmount = findViewById(R.id.eTxt_BudgetAmount);
        tvCatName = findViewById(R.id.tv_budgetCatName);
        radioGroup = findViewById(R.id.rg_BudgetRoleOver);
        rbMonthly = findViewById(R.id.radioButton_BudgetMonthly);
        rbWeekly  =findViewById(R.id.radioButton_BudgetWeekly);
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",-1);
    }

    public void showPopUpWindow(final View view){

        //create view with inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.activity_pop_up_image_pick, null);

        RecyclerView imgRecyclerView;
        ArrayList<Integer> imgRecyclerViewArrayList;
        TextView header;

        //Dimensions
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        //Create window
        final PopupWindow popUpWindow = new PopupWindow(popUpView, width,height,focusable);

        //Location
        popUpWindow.showAtLocation(view, Gravity.CENTER, 0,0);

        imgRecyclerView = popUpView.findViewById(R.id.rcv_ImagePick);
        header = popUpView.findViewById(R.id.txt_ImagePickHeader);

        //Get the image address
        imgRecyclerViewArrayList = new ArrayList<>();
        ImgDao imgDao = new ImgDao(this);
        for(String category: Constants.budgetImgNames){
            int address = imgDao.getImgByCategory(category);
            imgRecyclerViewArrayList.add(address);
        }

        //initialize adapter class and pass array list
        PopUpImagePickAdapter imgPickAdapter = new PopUpImagePickAdapter(this, imgRecyclerViewArrayList);

        //setting gird layout
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);

        //set layout manager and adapter to recycler view
        imgRecyclerView.setLayoutManager(layoutManager);
        imgRecyclerView.setAdapter(imgPickAdapter);

        imgPickAdapter.setOnItemClickListener(new PopUpImagePickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int address = imgRecyclerViewArrayList.get(position);
                imageView.setImageResource(address);
                tvCatName.setText(Constants.budgetImgNames[position]);
                popUpWindow.dismiss();
            }
        });

        //clicking on inactive part of screen
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //close window when click off
                popUpWindow.dismiss();
                return true;
            }
        });
    }


}