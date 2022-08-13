package com.cs673.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.dao.UserDao;
import com.cs673.myapplication.utils.EncryptPassword;
import com.cs673.myapplication.utils.ToastUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class RegisterActivity extends AppCompatActivity {

    EditText etName,etPwdOne,etPwdTwo,etEmail;
    TextInputEditText etQuestionOne,etQuestionTwo,etQuestionThree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    public void initView(){
        etName = findViewById(R.id.register_name);
        etPwdOne  = findViewById(R.id.register_pwd_one);
        etPwdTwo = findViewById(R.id.register_pwd_two);
        etEmail = findViewById(R.id.register_email);
        etQuestionOne = findViewById(R.id.etxt_RegFPQ1);
        etQuestionTwo = findViewById(R.id.etxt_RegFPQ2);
        etQuestionThree = findViewById(R.id.etxt_RegFPQ3);

    }

    public void registerOnClick(View view){
                String name = etName.getText().toString().trim();
                String pwdOne = etPwdOne.getText().toString().trim();
                String pwdTwo = etPwdTwo.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String questionOne = etQuestionOne.getText().toString().trim();
                String questionTwo = etQuestionTwo.getText().toString().trim();
                String questionThree = etQuestionThree.getText().toString().trim();

                if(name.isEmpty()){
                    ToastUtil.toastShort(this,"User name can not be null");
                    return;
                }
                if(pwdOne.isEmpty()||pwdTwo.isEmpty()){
                    ToastUtil.toastShort(this,"Password can not be null");
                    return;
                }
                if (!pwdOne.equals(pwdTwo)){
                    ToastUtil.toastShort(this,"Passwords do not match");
                    return;
                }
                if(name.length()<6){
                    ToastUtil.toastShort(this,"Name must have at least six characters");
                    return;
                }
                if(pwdOne.length()<6){
                    ToastUtil.toastShort(this,"password must have at least six numbers");
                    return;
                }

                if(questionOne.isEmpty()){
                    ToastUtil.toastShort(this,"Security question one can not be null!");
                    return;
                }

                if (questionTwo.isEmpty()){
                    ToastUtil.toastShort(this,"Security question two can not be null!");
                    return;
                }

                if(questionThree.isEmpty()){
                    ToastUtil.toastShort(this,"Security question three can not be null!");
                    return;
                }

                //hash password
                pwdOne = EncryptPassword.hashPassword(pwdOne);

                UserDao userDao = new UserDao(this);
                User user = new User();
                user.setName(name);
                user.setPassword(pwdOne);
                user.setEmail(email);
                user.setQuestionOne(questionOne);
                user.setQuestionTwo(questionTwo);
                user.setQuestionThree(questionThree);
                user.setShowInactiveGoal(0);//don't show inactive goal
                long rowId = userDao.insert(user);

                if(rowId==-1){
                    ToastUtil.toastShort(this,"UserName has been used");
                }else{
                    ToastUtil.toastShort(this,"Registration succeed");
                    Intent goToMain2 = new Intent(this,MainActivity.class);
                    startActivity(goToMain2);

                }
                return;

    }




}


