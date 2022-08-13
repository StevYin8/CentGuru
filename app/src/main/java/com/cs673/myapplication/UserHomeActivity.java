package com.cs673.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cs673.myapplication.bean.BadgeCardModel;
import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.dao.UserDao;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity {


    //the Recycler View
    RecyclerView rcvBadges;
    //Account Edit Button
    Button btnAccountEdit;
    TextView tvUserName;
    Switch hideUnearnedBadges,hideInactiveGoal;
    ArrayList<BadgeCardModel> badgeList;
    int userId = -1;
    int status = 0;//0: don't hide unearned, 1:hide
    int inactiveStatus = 0;//0: hide ,1:don't hide
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent getUserIdIntent = getIntent();
        userId = getUserIdIntent.getIntExtra("userId",-1);
        UserDao userDao = new UserDao(this);
        User user = userDao.queryByUserId(userId);

        tvUserName = findViewById(R.id.txt_UserHomeUserName);
        tvUserName.setText(user.getName());
        //Toolbar
        Toolbar toolbar =  findViewById(R.id.tb_userHome);
        setSupportActionBar(toolbar);
        hideInactiveGoal = findViewById(R.id.switch_HideGoalsInactive);
        inactiveStatus = user.getShowInactiveGoal();
        if (inactiveStatus==0){
            hideInactiveGoal.setChecked(true);
        }
        hideUnearnedBadges = findViewById(R.id.switch_HideUnearnedBadges);
        hideUnearnedBadges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    status =1;
                    showBadges(userDao);
                }else {
                    status=0;
                    showBadges(userDao);
                }
            }
        });

        hideInactiveGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    inactiveStatus =0;
                    userDao.updateStatus(inactiveStatus,userId);
                }else {
                    inactiveStatus=1;
                    userDao.updateStatus(inactiveStatus,userId);
                }

            }
        });

        //Badge List
        rcvBadges = findViewById(R.id.rcv_Badges);
        showBadges(userDao);



        //Account Edit Button
        btnAccountEdit = findViewById(R.id.btn_EditUserAccount);

        btnAccountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direct to the register page
                Intent goToAccountEdit = new Intent(getApplicationContext(),AccountEditActivity.class);
                goToAccountEdit.putExtra("userId",userId);
                startActivity(goToAccountEdit);

            }
        });
    }

    private void showBadges(UserDao userDao){
        if(status==0) {
            badgeList = userDao.getBadges(userId);
        }else {
            badgeList = userDao.getActiveBadges(userId);
        }

        //initialize adapter class and pass array list
        BadgeAdapter badgeAdapter = new BadgeAdapter(this, badgeList);

        //setting gird layout
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        //set layout manager and adapter to recycler view

        rcvBadges.setLayoutManager(layoutManager);
        rcvBadges.setAdapter(badgeAdapter);

    }


    //For Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuTop = getMenuInflater();
        menuTop.inflate(R.menu.action_bar_home,menu);

        return super.onCreateOptionsMenu(menu);

    }

    //For Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_main_page)
        {
            Intent goToMainActivity2 = new Intent(this, MainActivity2.class);
            //Start Activity
            goToMainActivity2.putExtra("userId",userId);
            startActivity(goToMainActivity2);
        }

        return super.onOptionsItemSelected(item);
    }
}