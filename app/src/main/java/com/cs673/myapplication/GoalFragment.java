package com.cs673.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs673.myapplication.bean.GoalCardModel;
import com.cs673.myapplication.bean.User;
import com.cs673.myapplication.dao.SavingsDao;
import com.cs673.myapplication.dao.UserDao;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends Fragment {

    private ArrayList<GoalCardModel> gCM;

    public GoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance() {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //the Recycler View
    RecyclerView rcvGoals;

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
        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        // instantiate recycler view
        rcvGoals = view.findViewById(R.id.rcv_Goals);

        Intent intent =getActivity().getIntent();
        int userId = intent.getIntExtra("userId", -1);
        UserDao userDao = new UserDao(getContext());
        User user = userDao.queryByUserId(userId);
        int status = user.getShowInactiveGoal();
        //get all goals from database
        SavingsDao goalDao = new SavingsDao(getContext());
        if(status==0) {
             gCM = goalDao.selectAll(userId);
        }else{
            gCM = goalDao.selectAllGoals(userId);
        }
        //initialize adapter class and pass array list
        GoalAdapter goalAdapter = new GoalAdapter(getContext(), gCM,userId);

        //set Linear Layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        //set layout manager and adapter to recycler view
        rcvGoals.setLayoutManager(linearLayoutManager);
        rcvGoals.setAdapter(goalAdapter);

        return view;

    }
}