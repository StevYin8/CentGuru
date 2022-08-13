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
import com.cs673.myapplication.utils.GenerateID;
import com.cs673.myapplication.utils.ToastUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GoalEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    EditText etGoalName,etGoalAmount,etGoalNotes;
    Button btnPickDate,btnSave,btnDelete;
    TextView tvShowDate,tvCategory;
    Button btnImagePopUp;
    ImageView imageView;
    int userId= -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_entry);
        initView();


        //To save a goal into the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Savings goal = new Savings();
                SavingsDao goalDao = new SavingsDao(getApplicationContext());
                String name = etGoalName.getText().toString().trim();
                String category = tvCategory.getText().toString().trim();
                String targetAmount = etGoalAmount.getText().toString().trim();
                String note = etGoalNotes.getText().toString().trim();
                String date=tvShowDate.getText().toString();

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

                goal.setName(name);
                goal.setCategory(category);
                goal.setTargetAmount(targetAmount);
                goal.setTargetDate(date);
                goal.setNote(note);
                goal.setStatus(1);
                goal.setUserId(userId);

                long result = goalDao.insertGoal(goal);
                if (result == -1 ){
                    Toast.makeText(getApplicationContext(), "Failed to insert a goal", Toast.LENGTH_SHORT).show();
                }
                else{
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
        btnImagePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(v);

            }
        });

    }

    public void initView(){
        //set up view
        etGoalName = findViewById(R.id.eTxt_GoalName);
        etGoalAmount = findViewById(R.id.eTxt_GoalAmount);
        etGoalNotes = findViewById(R.id.eTxt_GoalNotes);
        btnPickDate = findViewById(R.id.btn_TargetDatePick);
        btnSave = findViewById(R.id.btn_GoalSave);
        btnDelete = findViewById(R.id.btn_GoalDelete);
        tvShowDate = findViewById(R.id.txt_ShowTargetDate);
        tvCategory = findViewById(R.id.tv_GoalEntry_category);
        btnImagePopUp = findViewById(R.id.btn_GoalImagePick);
        imageView = findViewById(R.id.img_GoalEntryIcon);
        Intent intent =getIntent();
        userId = intent.getIntExtra("userId", -1);
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
        tvShowDate.setText(pickedDate);
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
                imageView.setImageResource(address);
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