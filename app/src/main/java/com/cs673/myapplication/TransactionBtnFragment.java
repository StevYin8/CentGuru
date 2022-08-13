package com.cs673.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cs673.myapplication.databinding.FragmentTransactionBtnBinding;

public class TransactionBtnFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Button
    //private Button makeTransactionBtn;

    //Binding
    private FragmentTransactionBtnBinding binding;


    public TransactionBtnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionBtnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionBtnFragment newInstance(String param1, String param2) {
        TransactionBtnFragment fragment = new TransactionBtnFragment();
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
        binding = FragmentTransactionBtnBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;


        //return inflater.inflate(R.layout.fragment_transaction_btn, container, false);
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
        //makeTransactionBtn = (Button) view.findViewById(R.id.btn_newTransaction);
        binding.btnNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FOR TESTING OF TRANSACTION ENTRY ACTIVITY
                Intent intent = getActivity().getIntent();
                int userId = intent.getIntExtra("userId",-1);
                Intent goToTransactionEntry = new Intent(getContext(), TransactionEntry.class);
                goToTransactionEntry.putExtra("userId",userId);
                //Start Activity
                startActivity(goToTransactionEntry);
            }
        });


    }
}
