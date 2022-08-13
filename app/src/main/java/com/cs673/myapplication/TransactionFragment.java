package com.cs673.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs673.myapplication.bean.TransactionCardModel;
import com.cs673.myapplication.dao.TransactionDao;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {


    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GoalFragment.
     */
    public static TransactionFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //the Recycler View
    RecyclerView rcvTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        // instantiate recycler view
        rcvTransaction = view.findViewById(R.id.rcv_Transactions);

        //get userId
        Intent intent = getActivity().getIntent();
        int userId = intent.getIntExtra("userId", -1);

        //get all transactions from database
        TransactionDao transactionDao = new TransactionDao(getContext());

        ArrayList<TransactionCardModel> tCM = transactionDao.selectAll(userId);

        //initialize adapter class and pass array list
        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), tCM,userId);

        //set Linear Layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        //set layout manager and adapter to recycler view
        rcvTransaction.setLayoutManager(linearLayoutManager);
        rcvTransaction.setAdapter(transactionAdapter);

        return view;

    }

}