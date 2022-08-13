package com.cs673.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cs673.myapplication.bean.BadgeCardModel;
import com.cs673.myapplication.bean.BudgetCardModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeCardViewholder> {


    private Context context;
    private ArrayList<BadgeCardModel> badgeCardModelArrayList;




    //Constructor
    public BadgeAdapter (Context context, ArrayList<BadgeCardModel> badgeCardModelArrayList)
    {
        this.context = context;
        this.badgeCardModelArrayList = badgeCardModelArrayList;

    }

    //inflate card layouts in recycler view
    @NonNull
    @Override
    public BadgeAdapter.BadgeCardViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_badge, parent, false);

        return new BadgeAdapter.BadgeCardViewholder(view);
    }

    //Set data for elements of card layouts
    @Override
    public void onBindViewHolder(@NonNull BadgeAdapter.BadgeCardViewholder holder, int position) {

        BadgeCardModel badge = badgeCardModelArrayList.get(position);
        holder.txtBadgeName.setText(badge.getName());
        holder.imgBadgeImage.setImageResource(badge.getImg());


        //if the badge is not earned tint image gray
        if(!badge.isEarned()){
            int colorGray = ContextCompat.getColor(context, R.color.grey);
            ImageViewCompat.setImageTintList(holder.imgBadgeImage, ColorStateList.valueOf(colorGray));

            int colorDarkGray = ContextCompat.getColor(context, android.R.color.darker_gray);
            holder.cvBadge.setCardBackgroundColor(colorDarkGray);


        }





    }



    //Get Number of Items
    @Override
    public int getItemCount() {
        return badgeCardModelArrayList.size();
    }



    //View Holder Class
    public class BadgeCardViewholder extends RecyclerView.ViewHolder {
        private CardView cvBadge;
        private TextView txtBadgeName;
        private ImageView imgBadgeImage;


        public BadgeCardViewholder(@NonNull View itemView) {
            super(itemView);
            cvBadge = itemView.findViewById(R.id.cv_badge);
            txtBadgeName = itemView.findViewById(R.id.txt_badge);
            imgBadgeImage = itemView.findViewById(R.id.img_badge);


            //Set On Click Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    //FOR TESTING OF BUDGET EDIT ACTIVITY
                    String path = null;
                    try {
                        path = saveCardImg(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        boolean saved = saveToMedia(path, "badge");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //NEED THIS TO SEND BADGE OUT IF POSSIBLE
                    //Intent goToBudgetEdit = new Intent(context, BudgetEdit.class);
                    //goToBudgetEdit.putExtra("budgetId",badgeCardModelArrayList.get(getPosition()).getId());
                    //Start Activity
                    //context.startActivity(goToBudgetEdit);
                }
            });

        }


    }


    //Save badge card image
    public String saveCardImg (View card) throws IOException {

        //set layout content
        card.setDrawingCacheEnabled(true);
        Bitmap bitmap = card.getDrawingCache();
        File nfile = null;
        try {
            nfile = File.createTempFile("badge.jpeg", null, context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            FileOutputStream fos = new FileOutputStream(nfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, fos);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        //path to image file saved
        String path = nfile.getPath();

        return path;

    }

    //save image from cache to media store
    public boolean saveToMedia (String path, String badgeName)
    {
        boolean done = false;

        File nFile = new File(path);


        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), String.valueOf(nFile), badgeName, badgeName);
            Toast imageSaved = Toast.makeText(context,"Badge Added to Device Image Gallery",Toast.LENGTH_LONG);
            imageSaved.show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return done;
    }

}
