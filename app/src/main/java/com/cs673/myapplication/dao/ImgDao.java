package com.cs673.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cs673.myapplication.bean.Image;
import com.cs673.myapplication.utils.Constants;
import com.cs673.myapplication.utils.DBHelper;

public class ImgDao {

    private DBHelper dbHelper;

    public ImgDao(Context context){
        dbHelper = DBHelper.getInstance(context);
    }

    //Insert images into the database
    public long insertImg(String category,long img){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.CAT,category);
        values.put(Constants.IMG,img);
        long result = db.insert(Constants.IMG_CAT_TABLE_NAME,null,values);
        db.close();
        return result;
    }

    //get the image address from database
    public int getImgByCategory(String category){
        int img = 0;
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Constants.IMG_CAT_TABLE_NAME + " where "
                + Constants.CAT + "=?", new String[]{category});
        while (cursor.moveToNext()){
            img  = cursor.getInt(1);
        }
        cursor.close();
        db.close();
        return img;
    }


}
