package com.cs673.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.BudgetDropDownModel;
import com.cs673.myapplication.bean.GoalDropDownModel;
import com.cs673.myapplication.bean.Savings;
import com.cs673.myapplication.bean.Transaction;
import com.cs673.myapplication.dao.BudgetDao;
import com.cs673.myapplication.dao.SavingsDao;
import com.cs673.myapplication.dao.TransactionDao;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.GenerateID;
import com.cs673.myapplication.utils.ToastUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class TransactionEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //Button to pick date
    Button btnPickDate;
    //Buttons to Save and Discard
    Button btnSave;
    Button btnCancel;

    //Radio Group
    RadioGroup rgTransactionType;
    RadioButton rbSaving;
    RadioButton rbSpending;

    //Edit Text to display date
    TextView txtShowDate;
    //Edit Text on Transaction Page
    EditText eTxt_Amount;
    EditText eTxt_Notes;

    Spinner goalSpinner,budgetSpinner;

    //type of transaction
    //0:spending 1:saving
    int type =0;

    //Goal selected position from drop down
    int goalPosition = 0;
    int budgetPosition = 0;
    int userId =-1;

    private ArrayList<GoalDropDownModel> goalList = new ArrayList<>();
    private ArrayList<String> goalNameList= new ArrayList<>();
    private ArrayList<BudgetDropDownModel> budgetList = new ArrayList<>();
    private ArrayList<String> budgetNameList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_entry);
        initView();
        initData();

        TransactionDao transactionDao = new TransactionDao(this);
        Transaction transaction = new Transaction();

        //Set onClickListener for radioGroup
        //1:saving , 0:spending
        rgTransactionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_TransactionSaving:
                        type = 1;
                        break;
                    case R.id.rb_TransactionSpending:
                        type = 0;
                        break;
                    default:
                        Log.i(Constants.TAG,"Radio group error!");
                        break;
                }
            }
        });

        //get goals from database;
        SavingsDao goalDao = new SavingsDao(this);
        goalList = goalDao.selectGoalNames(userId);
        goalNameList.add("Select a goal to connect...");
        for (GoalDropDownModel goal : goalList){
            goalNameList.add(goal.getGoalName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalNameList){
            @Override
            public boolean isEnabled(int position) {
                if (position==0) return false;
                else return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position,convertView,parent);
                    TextView textView = (TextView) view;
                    if(position==0){
                        textView.setTextColor(getResources().getColor(R.color.grey));
                    }
                    else textView.setTextColor(getResources().getColor(R.color.black));
                    return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(adapter);

        //Item selected listener for drop down
        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    goalPosition = i;
                    adapterView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setVisibility(View.VISIBLE);
            }
        });

        //get goals from database;
        BudgetDao budgetDao = new BudgetDao(this);
        budgetList = budgetDao.selectBudgetNames(userId);
        budgetNameList.add("Select a budget to connect...");
        for (BudgetDropDownModel budget : budgetList){
            budgetNameList.add(budget.getName());
        }

        ArrayAdapter<String> budgetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, budgetNameList){
            @Override
            public boolean isEnabled(int position) {
                if (position==0) return false;
                else return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView textView = (TextView) view;
                if(position==0){
                    textView.setTextColor(getResources().getColor(R.color.grey));
                }
                else textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        budgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(budgetAdapter);

        //Item selected listener for drop down
        budgetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    budgetPosition = i;
                    adapterView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setVisibility(View.VISIBLE);
            }
        });




        //Set On Click Listener for Save Button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountStr =  eTxt_Amount.getText().toString();
                String date=txtShowDate.getText().toString();
                String note = eTxt_Notes.getText().toString();
                //Link to a goal or a budget
                if(goalPosition!=0){
                  int goalId = goalList.get(goalPosition-1).getId();
                  transaction.setSavingsId(goalId);

                }
                if(budgetPosition!=0){
                    int budgetId = budgetList.get(budgetPosition-1).getId();
                    transaction.setBudgetId(budgetId);

                }



                if(date==null||date.length()==0){
                    ToastUtil.toastShort(TransactionEntry.this,"Please pick a date!");
                    return;
                }

                if(amountStr==null|amountStr.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please input amount!");
                    return;
                }
                float amount = Float.valueOf(amountStr);

                transaction.setType(type);
                transaction.setAmount(amount);
                transaction.setNote(note);
                transaction.setDate(date);
                transaction.setUserId(userId);

                long result = transactionDao.insertTransaction(transaction);
                if (result == -1 ){
                    Toast.makeText(getApplicationContext(), "Failed to insert a transaction", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
            }
        });


        //Set OnclickListener for Button to Delete
        btnCancel.setOnClickListener(new View.OnClickListener() {
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


    }

    public void initView(){
        //Set up view Elements
        btnPickDate = findViewById(R.id.btn_DatePick);
        txtShowDate = findViewById(R.id.txt_ShowDate);
        btnSave = findViewById(R.id.btn_Save);
        btnCancel = findViewById(R.id.btn_TransEntry_Cancel);
        eTxt_Amount =  findViewById(R.id.eTxt_Amount);
        eTxt_Notes = findViewById(R.id.eTxt_Notes);
        rgTransactionType = findViewById(R.id.rg_TransactionType);
        rbSaving = findViewById(R.id.rb_TransactionSaving);
        rbSpending = findViewById(R.id.rb_TransactionSpending);
        goalSpinner = findViewById(R.id.dd_goal);
        budgetSpinner = findViewById(R.id.dd_budget);

    }

    public void initData(){
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",-1);
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
        txtShowDate.setText(pickedDate);
    }
}