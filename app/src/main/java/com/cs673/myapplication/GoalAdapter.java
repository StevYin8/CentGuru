package com.cs673.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs673.myapplication.bean.GoalCardModel;

import java.util.ArrayList;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalCardViewholder> {

    private Context context;
    private ArrayList<GoalCardModel> goalCardModelArrayList;
    private int userId;

    //Constructor
    public GoalAdapter (Context context, ArrayList<GoalCardModel> goalCardModelArrayList,int userId)
    {
        this.context = context;
        this.goalCardModelArrayList = goalCardModelArrayList;
        this.userId = userId;
    }

    //inflate card layouts in recycler view
    @NonNull
    @Override
    public GoalAdapter.GoalCardViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goal, parent, false);

        return new GoalCardViewholder(view);
    }

    //Set data for elements of card layouts
    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.GoalCardViewholder holder, int position) {


        GoalCardModel goalMod = goalCardModelArrayList.get(position);
        holder.txtGoalName.setText(goalMod.getGoalName());
        String currentAmount = goalMod.getCurrentAmt();
        holder.txtCurrentAmt.setText(currentAmount);
        String targetAmount = goalMod.getTargetAmt();
        holder.txtTargetAmt.setText(targetAmount);
        if(Float.valueOf(currentAmount)<0){
            holder.txtCurrentAmt.setTextColor(holder.txtCurrentAmt.getResources().getColor(R.color.red));
            holder.imgGoalWarning.setImageResource(R.drawable.warning);
        }
        if(Float.valueOf(currentAmount)>=Float.valueOf(targetAmount)){
            holder.imgGoalWarning.setImageResource(R.drawable.medal);
        }
        holder.numProgress.setProgress(goalMod.getProgress());
        holder.imgGoalImage.setImageResource(goalMod.getImage());
        holder.txtTargetDate.setText(goalMod.getTargetDate());
    }



    //Get Number of Items
    @Override
    public int getItemCount() {
        return goalCardModelArrayList.size();
    }



    //View Holder Class
    public class GoalCardViewholder extends RecyclerView.ViewHolder {
        private TextView txtGoalName;
        private TextView txtCurrentAmt;
        private TextView txtTargetAmt;
        private ProgressBar numProgress;
        private ImageView imgGoalImage,imgGoalWarning;
        private TextView txtTargetDate;

        public GoalCardViewholder(@NonNull View itemView) {
            super(itemView);
            txtGoalName = itemView.findViewById(R.id.txt_GoalName);
            txtCurrentAmt = itemView.findViewById(R.id.txt_CurrentGoalAmt);
            txtTargetAmt = itemView.findViewById(R.id.txt_TargetGoalAmt);
            numProgress = itemView.findViewById(R.id.pBar_Goal);
            imgGoalImage = itemView.findViewById(R.id.img_GoalFrag_Icon);
            txtTargetDate = itemView.findViewById(R.id.txt_GoalTargetDate);
            imgGoalWarning = itemView.findViewById(R.id.img_GoalWarning);


            //Set On Click Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoalCardModel goalCardModel = goalCardModelArrayList.get(getPosition());
                    Intent goToGoalEdit = new Intent(context, GoalEdit.class);
                    //Start Activity
                    goToGoalEdit.putExtra("goalId",goalCardModel.getId());
                    goToGoalEdit.putExtra("userId",userId);
                    context.startActivity(goToGoalEdit);
                }
            });


        }


    }
}


