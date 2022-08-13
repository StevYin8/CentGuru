package com.cs673.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.dao.BudgetDao;
import com.cs673.myapplication.dao.UserDao;
import com.cs673.myapplication.utils.EncryptPassword;
import com.cs673.myapplication.utils.ToastUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    //Button to enter login credentials
    //For TESTING of TRANSACTION ENTRY just changes activities
    Button btnEnterLogin;

    //User Name Input
    EditText txtInUserName;
    //Password Input
    EditText txtInPassword;
    //Forgot Password
    TextView txtForgotPassword;

    //Button to make a new user
    Button btnNewUser;
    Intent intent = new Intent();

    //Input of user_name and password
    private String userName;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){

        btnEnterLogin = findViewById(R.id.btn_EnterLogin);
        txtInUserName = findViewById(R.id.user_name_entry_txt);
        txtInPassword = findViewById(R.id.eTxtNumberPassword);
        txtForgotPassword = findViewById(R.id.txt_forgot_password);
        btnNewUser = findViewById(R.id.btn_new_account);



        UserDao userDao = new UserDao(this);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direct to the register page
                Intent goToForgotPassword = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(goToForgotPassword);

            }
        });


        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direct to the register page
                Intent goToRegister = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(goToRegister);

            }
        });

        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input of username and password
                RegisterActivity registerActivity = new RegisterActivity();
                userName = txtInUserName.getText().toString().trim();
                password = txtInPassword.getText().toString().trim();
                String encryptedPassword = EncryptPassword.hashPassword(password);
                if(userName.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please input user name!");
                    return;
                }
                if (password.length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"Please input password!");
                    return;
                }
                User user = userDao.queryByUserName(userName);
                Log.i("LogIn",user.toString());
                if(user.getName()==null||user.getName().length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"User name or password wrong");
                    return;
                }else if(user.getPassword()==null||user.getPassword().length()==0){
                    ToastUtil.toastShort(getApplicationContext(),"User name or password wrong");
                    return;
                }else if(!user.getPassword().equals(encryptedPassword)){
                    ToastUtil.toastShort(getApplicationContext(),"User name or password wrong");
                    return;
                }else{
                    Intent goToMain2= new Intent(getApplicationContext(),MainActivity2.class);
                    goToMain2.putExtra("userId",user.getId());
                    startActivity(goToMain2);
                }
            }
        });




    }





}
