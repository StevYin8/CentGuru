package com.cs673.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.cs673.myapplication.databinding.ActivityMain2Binding;

import java.util.Map;


public class MainActivity2 extends AppCompatActivity {


    //View Binding
    ActivityMain2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //Binding
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //Toolbar
        Toolbar toolbar =  findViewById(R.id.tb_main2);
        setSupportActionBar(toolbar);




        //Initial Fragments
        //Fragment Button
        switchBtnFragment(new TransactionBtnFragment());
        //Fragment List
        switchListFragment(new TransactionFragment());



        //Changing Fragments by Bottom Navigation
        binding.bnbMainNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.mm_Transaction:
                    switchBtnFragment(new TransactionBtnFragment());
                    switchListFragment(new TransactionFragment());
                    break;
                case R.id.mm_Goal:
                    switchBtnFragment(new GoalBtnFragment());
                    switchListFragment(new GoalFragment());
                    break;
                case R.id.mm_Budget:
                    //TEST UNTIL WE HAVE BUDGET OPTION
                    switchBtnFragment(new BudgetBtnFragment());
                    switchListFragment(new BudgetFragment());
                    break;
            }

            return true;
        });



    }





    //For Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuTop = getMenuInflater();
        menuTop.inflate(R.menu.action_bar,menu);

        return super.onCreateOptionsMenu(menu);

    }

    //For Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_user)
        {
            Intent goToUserHome = new Intent(this, UserHomeActivity.class);
            //Start Activity
            goToUserHome.putExtra("userId",getIntent().getIntExtra("userId",-1));
            startActivity(goToUserHome);
        }

        return super.onOptionsItemSelected(item);
    }



    //Switching to a new button fragment
    private void switchBtnFragment (Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fcv_mainButton, fragment);
        fragmentTransaction.commit();
    }

    //Switching to a new List fragment
    private void switchListFragment (Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fcv_mainList, fragment);
        fragmentTransaction.commit();
    }


}