package com.cs673.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.BudgetCardModel;
import com.cs673.myapplication.dao.BudgetDao;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {



    public BudgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance() {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //the Recycler View
    RecyclerView rcvBudgets;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        // instantiate recycler view
        rcvBudgets = view.findViewById(R.id.rcv_Budgets);

        Intent intent =getActivity().getIntent();
        int userId = intent.getIntExtra("userId", -1);

        //make data array list
        BudgetDao dao = new BudgetDao(getContext());
        ArrayList<BudgetCardModel> bCM = dao.selectAll(userId);

        //initialize adapter class and pass array list
        BudgetAdapter budgetAdapter = new BudgetAdapter(getContext(), bCM,userId);

        //set Linear Layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        //set layout manager and adapter to recycler view
        rcvBudgets.setLayoutManager(linearLayoutManager);
        rcvBudgets.setAdapter(budgetAdapter);

        return view;

    }
}