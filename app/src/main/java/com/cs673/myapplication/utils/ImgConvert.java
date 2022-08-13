package com.cs673.myapplication.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImgConvert {


    //This method is used to convert an image to bitmap in order to store the images in the database
    @Deprecated
    public static byte[] pngToByte(String imgName, Context context){
        ApplicationInfo Appinfo = context.getApplicationInfo();
        int resId= context.getResources().getIdentifier(imgName,"drawable",Appinfo.packageName);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        byte[] byteImg = byteArrayOutputStream.toByteArray();
        return byteImg;

    }

}
