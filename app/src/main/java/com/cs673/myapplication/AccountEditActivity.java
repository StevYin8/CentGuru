package com.cs673.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.dao.UserDao;
import com.cs673.myapplication.utils.EncryptPassword;
import com.cs673.myapplication.utils.ToastUtil;
import com.google.android.material.textfield.TextInputEditText;

public class AccountEditActivity extends AppCompatActivity {

    //Save Button
    Button btnSave;
    EditText etName,etPwdOne,etPwdTwo,etEmail;
    TextInputEditText txtQOne,txtQTwo,txtQThree;
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        initView();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setId(userId);
                String name = etName.getText().toString().trim();
                String pwdOne = etPwdOne.getText().toString().trim();
                String pwdTwo = etPwdTwo.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String questionOne = txtQOne.getText().toString().trim();
                String questionTwo = txtQTwo.getText().toString().trim();
                String questionThree = txtQThree.getText().toString().trim();
                if(name.isEmpty()){
                    ToastUtil.toastShort(getApplicationContext(),"User name can not be null");
                    return;
                }
                if(pwdOne.isEmpty()||pwdTwo.isEmpty()){
                    ToastUtil.toastShort(getApplicationContext(),"Password can not be null");
                    return;
                }
                if (!pwdOne.equals(pwdTwo)){
                    ToastUtil.toastShort(getApplicationContext(),"Passwords do not match");
                    return;
                }
                if(name.length()<6){
                    ToastUtil.toastShort(getApplicationContext(),"Name must have at least six characters");
                    return;
                }
                if(pwdOne.length()<6){
                    ToastUtil.toastShort(getApplicationContext(),"password must have at least six numbers");
                    return;
                }

                if(questionOne.isEmpty()){
                    ToastUtil.toastShort(getApplicationContext(),"Security question one can not be null!");
                    return;
                }

                if (questionTwo.isEmpty()){
                    ToastUtil.toastShort(getApplicationContext(),"Security question two can not be null!");
                    return;
                }

                if(questionThree.isEmpty()){
                    ToastUtil.toastShort(getApplicationContext(),"Security question three can not be null!");
                    return;
                }
                user.setName(name);
                user.setPassword(EncryptPassword.hashPassword(pwdOne));
                user.setEmail(email);
                user.setQuestionOne(questionOne);
                user.setQuestionTwo(questionTwo);
                user.setQuestionThree(questionThree);
                UserDao userDao = new UserDao(getApplicationContext());
                userDao.updateUser(user);
                //Direct to the register page
                Intent goToMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goToMain);

            }
        });

//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goToUserHome = new Intent(getApplicationContext(),UserHomeActivity.class);
//                goToUserHome.putExtra("userId",userId);
//                startActivity(goToUserHome);
//            }
//        });
    }

    private void initView(){
        Intent intent =getIntent();
        userId = intent.getIntExtra("userId",-1);
        UserDao userDao = new UserDao(this);
        User user = userDao.queryByUserId(userId);
        //Save Button
        btnSave = findViewById(R.id.btn_AccountSave);
//        btnCancel = findViewById(R.id.btn_AccountCancel);
        etName = findViewById(R.id.Acct_name);
        etName.setText(user.getName());
        etPwdOne = findViewById(R.id.Acct_pwd_one);
        etPwdTwo = findViewById(R.id.Acct_pwd_two);
        etEmail = findViewById(R.id.Acct_email);
        etEmail.setText(user.getEmail());
        txtQOne = findViewById(R.id.etxt_AcctFPQ1);
        txtQOne.setText(user.getQuestionOne());
        txtQTwo = findViewById(R.id.etxt_AcctFPQ2);
        txtQTwo.setText(user.getQuestionTwo());
        txtQThree = findViewById(R.id.etxt_AcctFPQ3);
        txtQThree.setText(user.getQuestionThree());
    }


}