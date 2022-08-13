package com.cs673.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class PopUpImagePickAdapter extends RecyclerView.Adapter<PopUpImagePickAdapter.PopUpImageCardViewholder> {

    private Context context;
    private ArrayList<Integer> popUpImageArrayList;
    private OnItemClickListener onItemClickListener;

    //Constructor
    public PopUpImagePickAdapter (Context context, ArrayList<Integer> popUpImageArrayList)
    {
        this.context = context;
        this.popUpImageArrayList = popUpImageArrayList;

    }



    //inflate card layouts in recycler view
    @NonNull
    @Override
    public PopUpImagePickAdapter.PopUpImageCardViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image_pick, parent, false);

        return new PopUpImageCardViewholder(view);
    }

    //Set data for elements of card layouts
    @Override
    public void onBindViewHolder(@NonNull PopUpImagePickAdapter.PopUpImageCardViewholder holder, int position) {

        int imageMod = popUpImageArrayList.get(position);
        holder.imagePickImage_view.setImageResource(imageMod);

        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,position);

                }
            });
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }




    //Get Number of Items
    @Override
    public int getItemCount() {
        return popUpImageArrayList.size();
    }




    //View Holder Class
    public class PopUpImageCardViewholder extends RecyclerView.ViewHolder {
        private ImageView imagePickImage_view;

        public PopUpImageCardViewholder(@NonNull View itemView) {
            super(itemView);
            imagePickImage_view = itemView.findViewById(R.id.img_Icon);
        }

    }

}