package com.cs673.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.cs673.myapplication.databinding.ActivityAnalysisBinding;
import com.cs673.myapplication.databinding.ActivityMain2Binding;

public class AnalysisActivity extends AppCompatActivity {

    //View Binding
    ActivityAnalysisBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);




        //Binding
        binding = ActivityAnalysisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //Initial Fragments
        //Fragment Page
        switchPageFragment(new TransactionFragment());




        //TEST****************************************************************************************************************************************
        //Replace with Analysis fragments
        //Changing Fragments by Bottom Navigation
        binding.bnbAnalysisNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.mm_Transaction:
                    switchPageFragment(new TransactionFragment());
                    break;
                case R.id.mm_Goal:
                    switchPageFragment(new GoalFragment());
                    break;
                case R.id.mm_Budget:
                    switchPageFragment(new BudgetFragment());
                    break;
            }

            return true;
        });


    }



    //TEST****************************************************************************************************************************************
    //Replace with Analysis fragments
    //Switching to a new List fragment
    private void switchPageFragment (Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fcv_AnalysisPage, fragment);
        fragmentTransaction.commit();
    }
}