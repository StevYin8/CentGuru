package com.cs673.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs673.myapplication.bean.TransactionCardModel;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionCardViewHolder> {

    private List<com.cs673.myapplication.bean.TransactionCardModel> transactions;
    private Context context;
    private int userId;

    public TransactionAdapter(Context context, List<TransactionCardModel> transactions,int userId){
        this.context = context;
        this.transactions= transactions;
        this.userId = userId;
    }

    @NonNull
    @Override
    public TransactionCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction, parent, false);
        return new TransactionCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionCardViewHolder holder, int position) {
        int img_cat = transactions.get(position).getImage();
        String name = transactions.get(position).getName();
        String date = transactions.get(position).getDate();
        float amount = transactions.get(position).getAmount();
        holder.setData(img_cat, name, date, amount);

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionCardViewHolder extends  RecyclerView.ViewHolder{

        private ImageView img_CatIcon_view;
        private TextView cat_view;
        private TextView date_view;
        private TextView amount_view;





        public TransactionCardViewHolder(@NonNull View itemView) {
            super(itemView);

            img_CatIcon_view=itemView.findViewById(R.id.img_CatIcon1);
            cat_view=itemView.findViewById(R.id.catview);
            date_view=itemView.findViewById(R.id.dateview);
            amount_view=itemView.findViewById(R.id.amountview);


            //Set On Click Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransactionCardModel transaction = transactions.get(getPosition());
                    Intent goToTransactionEdit = new Intent(context, TransactionEdit.class);
                    goToTransactionEdit.putExtra("transactionId",transaction.getId());
                    goToTransactionEdit.putExtra("userId",userId);
                    context.startActivity(goToTransactionEdit);
                }
            });



        }

        public void setData(int img_cat, String category, String date, float amount) {
            img_CatIcon_view.setImageResource(img_cat);
            cat_view.setText(category);
            date_view.setText(date);
            amount_view.setText(Float.toString(amount));
        }



    }
}