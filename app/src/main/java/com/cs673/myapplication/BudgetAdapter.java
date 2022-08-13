package com.cs673.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs673.myapplication.bean.Budget;
import com.cs673.myapplication.bean.BudgetCardModel;
import com.cs673.myapplication.dao.ImgDao;


import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<com.cs673.myapplication.BudgetAdapter.BudgetCardViewholder> {

        private Context context;
        private ArrayList<BudgetCardModel> budgetCardModelArrayList;
        private int userId;

        //Constructor
        public BudgetAdapter (Context context, ArrayList<BudgetCardModel> budgetCardModelArrayList,int userId)
        {
            this.context = context;
            this.budgetCardModelArrayList = budgetCardModelArrayList;
            this.userId = userId;
        }

        //inflate card layouts in recycler view
        @NonNull
        @Override
        public BudgetAdapter.BudgetCardViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_budget, parent, false);

            return new BudgetCardViewholder(view);
        }

        //Set data for elements of card layouts
        @Override
        public void onBindViewHolder(@NonNull BudgetAdapter.BudgetCardViewholder holder, int position) {

            BudgetCardModel budget = budgetCardModelArrayList.get(position);
            holder.txtBudgetName.setText(budget.getName());
            holder.txtRoleOver.setText(budget.getTimeCycle());
            Float currentAmount = budget.getCurrentAmount();

            //prep CurrentAmt data for display
            Float txtCurAmt = currentAmount;
            if (currentAmount<0)
            {
                txtCurAmt = currentAmount*-1;
            }

            holder.txtCurrentAmt.setText(Float.toString(txtCurAmt));
            Float limitAmount = budget.getLimitAmount();
            holder.txtLimitAmt.setText(Float.toString(limitAmount));
            int progress = (int)(txtCurAmt/budget.getLimitAmount()*100);
            if(txtCurAmt>limitAmount){
                holder.txtCurrentAmt.setTextColor(holder.txtCurrentAmt.getResources().getColor(R.color.red));
                holder.imgWarning.setImageResource(R.drawable.warning);
            }
            holder.numProgress.setProgress(progress);
            holder.imgBudgetImage.setImageResource(budget.getImg());
            holder.txtEndDate.setText(budget.getEndDate());
        }



        //Get Number of Items
        @Override
        public int getItemCount() {
            return budgetCardModelArrayList.size();
        }



        //View Holder Class
        public class BudgetCardViewholder extends RecyclerView.ViewHolder {
            private TextView txtBudgetName;
            private TextView txtRoleOver;
            private TextView txtCurrentAmt;
            private TextView txtLimitAmt;
            private ProgressBar numProgress;
            private ImageView imgBudgetImage,imgWarning;
            private TextView txtEndDate;

            public BudgetCardViewholder(@NonNull View itemView) {
                super(itemView);
                txtBudgetName = itemView.findViewById(R.id.txt_BudgetName);
                txtRoleOver = itemView.findViewById(R.id.txt_RoleOver);
                txtCurrentAmt = itemView.findViewById(R.id.txt_CurrentBudgetAmt);
                txtLimitAmt = itemView.findViewById(R.id.txt_LimitBudgetAmt);
                numProgress = itemView.findViewById(R.id.pBar_Budget);
                imgBudgetImage = itemView.findViewById(R.id.img_BudgetIcon);
                txtEndDate = itemView.findViewById(R.id.txt_BudgetEndDate);
                imgWarning = itemView.findViewById(R.id.img_BudgetWarning);

               //Set On Click Listener
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //FOR TESTING OF BUDGET EDIT ACTIVITY
                        Intent goToBudgetEdit = new Intent(context, BudgetEdit.class);
                        goToBudgetEdit.putExtra("budgetId",budgetCardModelArrayList.get(getPosition()).getId());
                        //Start Activity
                        goToBudgetEdit.putExtra("userId",userId);
                        context.startActivity(goToBudgetEdit);
                    }
                });

            }


        }




}
