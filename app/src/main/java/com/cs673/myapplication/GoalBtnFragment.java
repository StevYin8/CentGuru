package com.cs673.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cs673.myapplication.databinding.FragmentGoalBtnBinding;
import com.cs673.myapplication.databinding.FragmentTransactionBtnBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalBtnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalBtnFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //button
    //private Button makeGoalBtn;


    //Binding
    private FragmentGoalBtnBinding binding;

    public GoalBtnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalBtnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalBtnFragment newInstance(String param1, String param2) {
        GoalBtnFragment fragment = new GoalBtnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Binding
        binding = FragmentGoalBtnBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;


        //return inflater.inflate(R.layout.fragment_goal_btn, container, false);
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set button
        //makeGoalBtn = (Button) view.findViewById(R.id.btn_newGoal);

        binding.btnNewGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FOR TESTING OF TRANSACTION ENTRY ACTIVITY
                Intent intent = getActivity().getIntent();
                int userId = intent.getIntExtra("userId", -1);
                Intent goToGoalEntry = new Intent(getContext(), GoalEntry.class);
                //Start Activity
                goToGoalEntry.putExtra("userId",userId);
                startActivity(goToGoalEntry);
            }
        });

        //Set onClickListener for Button to enter Login credentials
       /* makeGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FOR TESTING OF GOAL ENTRY ACTIVITY
                Intent goToGoalEntry = new Intent(getContext(), GoalEntry.class);
                //Start Activity
                startActivity(goToGoalEntry);

            }

        });*/

    }

}