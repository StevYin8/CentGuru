package com.cs673.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cs673.myapplication.bean.Savings;
import com.cs673.myapplication.dao.ImgDao;
import com.cs673.myapplication.dao.SavingsDao;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.ToastUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GoalEdit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText etName,etAmount,etNote;
    Button  btnImagePick,btnPickDate,btnSave,btnCancel,btnDelete;
    ImageView ivCategory;
    TextView tvCategory,tvDate;
    Switch switchStatus;
    int goalId = -1;
    String currentAmount;
    int status = 1;
    int userId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_edit);
        initView();
        initData();

        //Set onClickListener for Button to enter Date
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePickerDialogFragment;
                datePickerDialogFragment = new DatePicker();
                datePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        //Set on Click Listener for image pick button
        btnImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(v);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Savings goal = new Savings();
                SavingsDao goalDao = new SavingsDao(getApplicationContext());
                String name = etName.getText().toString().trim();
                String category = tvCategory.getText().toString().trim();
                String targetAmount = etAmount.getText().toString().trim();
                String note = etNote.getText().toString().trim();
                String date=tvDate.getText().toString();

                if(name==null||name.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please give a goal name!");
                    return;
                }

                if(category==null||category.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please choose a category!");
                    return;
                }

                if(date==null||date.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please pick a date!");
                    return;
                }

                if(targetAmount==null||targetAmount.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please input target amount!");
                    return;
                }
                goal.setId(goalId);
                goal.setName(name);
                goal.setCategory(category);
                goal.setTargetAmount(targetAmount);
                goal.setTargetDate(date);
                goal.setNote(note);
                goal.setStatus(status);

               goalDao.editGoal(goal);
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavingsDao goalDao = new SavingsDao(getApplicationContext());
                goalDao.deleteGoalById(goalId);
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    status =0;
                }else {
                    status=1;
                }
            }
        });

    }

    public void initView(){
        etName = findViewById(R.id.eTxt_GoalEditName);
        btnImagePick = findViewById(R.id.btn_GoalEditImagePick);
        ivCategory = findViewById(R.id.img_GoalEditIcon);
        tvCategory = findViewById(R.id.tv_GoalEdit_category);
        tvDate = findViewById(R.id.txt_EditShowTargetDate);
        btnPickDate = findViewById(R.id.btn_EditTargetDatePick);
        etAmount = findViewById(R.id.eTxt_GoalEditAmount);
        etNote = findViewById(R.id.eTxt_GoalEditNotes);
        btnSave = findViewById(R.id.btn_GoalEditSave);
        btnCancel = findViewById(R.id.btn_GoalEditCancel);
        btnDelete = findViewById(R.id.btn_GoalEditDelete);
        switchStatus = findViewById(R.id.switch_GoalActive);
    }

    public void initData(){
        Intent intent = getIntent();
        goalId = intent.getIntExtra("goalId",-1);
        userId = intent.getIntExtra("userId",-1);
        SavingsDao goalDao = new SavingsDao(this);
        ImgDao imgDao = new ImgDao(this);
        Savings goal = goalDao.selectGoalById(goalId);
        int img = imgDao.getImgByCategory(goal.getCategory());
        etName.setText(goal.getName());
        ivCategory.setImageResource(img);
        tvCategory.setText(goal.getCategory());
        tvDate.setText(goal.getTargetDate());
        etAmount.setText(goal.getTargetAmount());
        etNote.setText(goal.getNote());
        status = goal.getStatus();
        if(status==0){
            switchStatus.setChecked(true);
        }

    }

    //Set the result of the Date picker to the display text
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar nCalendar = Calendar.getInstance();
        nCalendar.set(Calendar.YEAR, year);
        nCalendar.set(Calendar.MONTH, month);
        nCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //Set display string with date
        String  pickedDate = DateFormat.getDateInstance(DateFormat.LONG).format(nCalendar.getTime());
        tvDate.setText(pickedDate);
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
        for(String category: Constants.goalImgNames){
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
                tvCategory.setText(Constants.goalImgNames[position]);
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