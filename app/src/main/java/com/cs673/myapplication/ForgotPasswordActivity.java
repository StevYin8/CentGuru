package com.cs673.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.dao.UserDao;
import com.cs673.myapplication.utils.ToastUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText etUserName,etQuestionOne,etQuestionTwo,etQuestionThree;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                if(userName==null||userName.isEmpty()){
                    ToastUtil.toastShort(ForgotPasswordActivity.this,"User name can not be null!");
                    return;
                }
                String questionOne = etQuestionOne.getText().toString().trim();
                if(questionOne==null||questionOne.isEmpty()){
                    ToastUtil.toastShort(ForgotPasswordActivity.this,"Security question can not be null!");
                    return;
                }
                String questionTwo = etQuestionTwo.getText().toString().trim();
                if(questionTwo==null||questionTwo.isEmpty()){
                    ToastUtil.toastShort(ForgotPasswordActivity.this,"Security question can not be null!");
                    return;
                }
                String questionThree = etQuestionThree.getText().toString().trim();
                if(questionThree==null||questionThree.isEmpty()){
                    ToastUtil.toastShort(ForgotPasswordActivity.this,"Security question can not be null!");
                    return;
                }
                UserDao userDao = new UserDao(getApplicationContext());
                User user = userDao.queryByUserName(userName);
                Log.i("test", "onClick: "+user.toString());
                if (user.getName()==null||user.getName().isEmpty()){
                    ToastUtil.toastShort(ForgotPasswordActivity.this,"User name does not exist!");
                    return;
                }
                if(!user.getQuestionOne().equals(questionOne)||!user.getQuestionTwo().equals(questionTwo)||!user.getQuestionThree().equals(questionThree)){
                    ToastUtil.toastShort(ForgotPasswordActivity.this,"The answer of security questions is wrong");
                    return;
                }else{
                    Intent goAccountEdit = new Intent(getApplicationContext(),AccountEditActivity.class);
                    goAccountEdit.putExtra("userId",user.getId());
                    startActivity(goAccountEdit);
                }

            }
        });

    }

    private void initView() {
        etUserName = findViewById(R.id.txt_ForgotPassword_user_name_entry);
        etQuestionOne = findViewById(R.id.etxt_ForgotPasswordQ1);
        etQuestionTwo = findViewById(R.id.etxt_ForgotPasswordQ2);
        etQuestionThree = findViewById(R.id.etxt_ForgotPasswordQ3);
        btnSubmit = findViewById(R.id.btn_ForgotPasswordSubmit);
    }


}