package com.cs673.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.cs673.myapplication.bean.Transaction;
import com.cs673.myapplication.bean.TransactionCardModel;
import com.cs673.myapplication.dao.BudgetDao;
import com.cs673.myapplication.dao.SavingsDao;
import com.cs673.myapplication.dao.TransactionDao;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.ToastUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionEdit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    RadioButton rbSpending,rbSaving;
    TextView tvShowDate;
    EditText etAmount,etNotes;
    Spinner goalSpinner,budgetSpinner;
    Button btnPickDate,btnCancel,btnSave,btnDelete;
    RadioGroup rgTransactionType;

    //Goal selected position from drop down
    int goalPosition = 0;
    int budgetPosition = 0;
    int type = 0;
    int transactionId = -1;
    int userId = -1;

    private ArrayList<GoalDropDownModel> goalList = new ArrayList<>();
    private ArrayList<String> goalNameList= new ArrayList<>();
    private ArrayList<BudgetDropDownModel> budgetList = new ArrayList<>();
    private ArrayList<String> budgetNameList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);
        initView();
        initData();

        TransactionDao transactionDao = new TransactionDao(getApplicationContext());
        Transaction transaction = new Transaction();

        //Set onClickListener for radioGroup
        //0:saving , 1:spending
        rgTransactionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_TransactionEditSaving:
                        type = 1;
                        break;
                    case R.id.rb_TransactionEditSpending:
                        type = 0;
                        break;
                    default:
                        Log.i("RadioGroup", "onCheckedChanged: error");
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
        goalSpinner.setSelection(goalPosition,true);

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
        budgetSpinner.setSelection(budgetPosition,true);

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

                String amountStr =  etAmount.getText().toString();
                String date=tvShowDate.getText().toString();
                String note =etNotes.getText().toString();
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
                    ToastUtil.toastShort(getApplicationContext(),"Please pick a date!");
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
                transaction.setId(transactionId);

                transactionDao.updateTransaction(transaction);


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

        //Set OnclickListener for Button to Delete
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
                transactionDao.deleteById(transactionId);
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
    }

    public void initView(){
        rbSpending = findViewById(R.id.rb_TransactionEditSpending);
        rbSaving = findViewById(R.id.rb_TransactionEditSaving);
        tvShowDate = findViewById(R.id.txt_TransEdit_ShowDate);
        etAmount = findViewById(R.id.eTxt_TransEdit_Amount);
        etNotes = findViewById(R.id.eTxt_TransEdit_Notes);
        btnPickDate = findViewById(R.id.btn_TransEdit_DatePick);
        btnCancel = findViewById(R.id.btn_TransEdit_Cancel);
        btnSave = findViewById(R.id.btn_TransEdit_Save);
        rgTransactionType = findViewById(R.id.rg_TransactionEditType);
        goalSpinner = findViewById(R.id.dd_TransEdit_goal);
        budgetSpinner = findViewById(R.id.dd_TransEdit_budget);
        btnDelete = findViewById(R.id.btn_TransEdit_Delete);
    }

    public void initData(){
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",-1);

        transactionId = intent.getIntExtra("transactionId", -1);
        if (transactionId!=-1){
            TransactionDao transactionDao = new TransactionDao(this);
            Transaction transaction = transactionDao.selectById(transactionId,userId);
            type = transaction.getType();
            if(type==0) {
                rgTransactionType.check(rbSpending.getId());
            }else {
                rgTransactionType.check(rbSaving.getId());
            }
            tvShowDate.setText(transaction.getDate());
            etAmount.setText(Float.toString(transaction.getAmount()));
            etNotes.setText(transaction.getNote());
            SavingsDao goalDao = new SavingsDao(this);
            goalPosition = goalDao.getCount(transaction.getSavingsId(),userId);
            BudgetDao budgetDao = new BudgetDao(this);
            budgetPosition = budgetDao.getCount(transaction.getBudgetId(),userId);

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
        tvShowDate.setText(pickedDate);
    }
}