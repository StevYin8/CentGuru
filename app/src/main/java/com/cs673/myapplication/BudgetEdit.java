package com.cs673.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.dao.BudgetDao;
import com.cs673.myapplication.dao.ImgDao;
import com.cs673.myapplication.utils.CalendarUtil;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.ToastUtil;
import com.cs673.myapplication.utils.WorkUtil;

import java.util.ArrayList;

public class BudgetEdit extends AppCompatActivity {

    EditText etName,etAmount;
    Button btnImagePick,btnSave,btnCancel,btnDelete;
    ImageView ivCategory;
    TextView tvCategory;
    RadioGroup radioGroup;
    RadioButton rbWeekly,rbMonthly;

    int budgetId= -1;
    int type = 0; //0: Monthly , 1: Weekly
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit);
        initView();
        initData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                if(name==null||name.length()==0){
                    ToastUtil.toastShort(BudgetEdit.this,"Please input budget name!");
                    return;
                }

                String category = tvCategory.getText().toString().trim();
                if(category==null||category.length()==0){
                    ToastUtil.toastShort(BudgetEdit.this,"Please select a category!");
                    return;
                }

                String amountStr =  etAmount.getText().toString().trim();
                if(amountStr==null||amountStr.length()==0){
                    ToastUtil.toastShort(BudgetEdit.this,"Please input target amount!");
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
                budget.setId(budgetId);
                budget.setName(name);
                budget.setCategory(category);
                budget.setLimitAmount(amount);
                //budget.setNote();
                BudgetDao dao = new BudgetDao(BudgetEdit.this);
                dao.updateBudget(budget);

                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("userId",userId);
                startActivity(intent);

                }

        });

        //Set OnclickListener for Button to Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetDao budgetDao = new BudgetDao(getApplicationContext());
                budgetDao.deleteById(budgetId);
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        //Set on Click Listener for image pick button
        btnImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(v);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_BudgetEditMonthly:
                        type = 0;
                        break;
                    case R.id.radioButton_BudgetEditWeekly:
                        type = 1;
                        break;
                    default:
                        Log.i(Constants.TAG,"Radio group error!");
                        break;
                }
            }
        });



    }

    public void initView(){
        etName = findViewById(R.id.eTxt_BudgetEditName);
        btnImagePick = findViewById(R.id.btn_BudgetEditImagePick);
        ivCategory = findViewById(R.id.img_BudgetEditIcon);
        tvCategory = findViewById(R.id.tv_budgetEditCatName);
        radioGroup = findViewById(R.id.rg_BudgetEditRoleOver);
        rbMonthly = findViewById(R.id.radioButton_BudgetEditMonthly);
        rbWeekly = findViewById(R.id.radioButton_BudgetEditWeekly);
        etAmount = findViewById(R.id.eTxt_BudgetEditAmount);
        btnSave = findViewById(R.id.btn_BudgetEditSave);
        btnCancel = findViewById(R.id.btn_BudgetEditCancel);
        btnDelete = findViewById(R.id.btn_BudgetEditDelete) ;
    }

    public void initData(){
        Intent intent =getIntent();
        budgetId = intent.getIntExtra("budgetId",-1);
        userId = intent.getIntExtra("userId",-1);
        BudgetDao budgetDao = new BudgetDao(this);
        Budget budget = budgetDao.selectBudgetById(budgetId);
        ImgDao imgDao = new ImgDao(this);
        int img= imgDao.getImgByCategory(budget.getCategory());
        etName.setText(budget.getName());
        ivCategory.setImageResource(img);
        tvCategory.setText(budget.getCategory());
        if(budget.getTimeCycle().equals("Monthly")){
            radioGroup.check(rbMonthly.getId());
        }else {
            radioGroup.check(rbWeekly.getId());
            type =1;
        }
        etAmount.setText(Float.toString(budget.getLimitAmount()));
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
                ivCategory.setImageResource(address);
                tvCategory.setText(Constants.budgetImgNames[position]);
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