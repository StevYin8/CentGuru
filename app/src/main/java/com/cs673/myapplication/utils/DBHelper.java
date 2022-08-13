package com.cs673.myapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.cs673.myapplication.R;


public class DBHelper extends SQLiteOpenHelper {
    private  Context context;
    private  static DBHelper myInstance;

    private DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    //Singleton
    public static DBHelper getInstance(Context context){
        if(myInstance==null){
            myInstance = new DBHelper(context,Constants.DATABASE_NAME,null, Constants.DATABASE_VERSION);
        }
        return myInstance;
    }



    //Initialize the database,only execute once
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user table
        String create_user=
                "CREATE TABLE " + Constants.USER_TABLE_NAME + " (" +
                        Constants.userId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                        Constants.COLUMN_USER_NAME + " VARCHAR(20) NOT NULL UNIQUE, " +
                        Constants.COLUMN_EMAIL_ADDRESS + " VARCHAR(50) NOT NULL, "+
                        Constants.COLUMN_PASSWORD + " INT NOT NULL,"+
                        Constants.QUESTION_ONE+" VARCHAR(140) NOT NULL,"+
                        Constants.QUESTION_TWO+" VARCHAR(140) NOT NULL,"+
                        Constants.QUESTION_Three+" VARCHAR(140) NOT NULL,"+
                        Constants.showInactiveGoal+" int NOT NULL);"
                ;
        db.execSQL(create_user);

        //create img table
        String create_ImgCategory=
                "CREATE TABLE " + Constants.IMG_CAT_TABLE_NAME+ " (" +
                        //Constants.IMG_ID + " INT NOT NULL, " +
                        Constants.CAT + " VARCHAR(20) NOT NULL, " +
                        Constants.IMG + " INT NOT NULL, " +
                        " PRIMARY KEY (" + Constants.CAT + ") );"
                ;
        db.execSQL(create_ImgCategory);

        // create transaction table
        String create_transaction=
                "CREATE TABLE " + Constants.TRANSACTION_TABLE_NAME+ " (" +
                        Constants.COLUMN_TRANSACTION_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        Constants.COLUMN_AMOUNT+ " DECIMAL(10,2) NOT NULL, " +
                        Constants.COLUMN_TYPE + " BOOLEAN NOT NULL, "+
                        Constants.COLUMN_DATE + " DATE NOT NULL, "+
                        Constants.COLUMN_REFERENCE_BUDGET_ID + " VARCHAR(20), " +
                        Constants.COLUMN_REFERENCE_SAVINGS_ID + " VARCHAR(20), " +
                        Constants.COLUMN_NOTE + " VARCHAR(140)," +
                        Constants.userId+" INTEGER NOT NULL,"+

                        " FOREIGN KEY (" + Constants.COLUMN_REFERENCE_BUDGET_ID +
                        ") REFERENCES " + Constants.BUDGET_TABLE_NAME + "(" + Constants.COLUMN_BUDGET_ID + ")," +
                        " FOREIGN KEY (" + Constants.COLUMN_REFERENCE_SAVINGS_ID + ")" +
                        " REFERENCES " + Constants.SAVINGS_TABLE_NAME + "(" + Constants.COLUMN_SAVINGS_ID + ")," +
                        " FOREIGN KEY ("+ Constants.userId+ ") " +
                        " REFERENCES "+Constants.USER_TABLE_NAME+ " ("+Constants.userId+") );"

                ;
        db.execSQL(create_transaction);

        // create budget table
        String create_budget=
                "CREATE TABLE " + Constants.BUDGET_TABLE_NAME + " (" +
                        Constants.COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        Constants.CAT+ " VARCHAR(20), " +
                        Constants.COLUMN_BUDGET_NAME + " VARCHAR(20) NOT NULL   , " +
                        Constants.COLUMN_TIME_CYCLE +  " DATE, "+
                        Constants.COLUMN_LIMIT_AMOUNT + " DECIMAL(10,2) NOT NULL, "+
                        Constants.COLUMN_BUDGET_NOTE + " VARCHAR(140)," +
                        Constants.COLUMN_END_DATE +" TEXT NOT NULL,"+
                        Constants.STATUS+" INTEGER NOT NULL,"+
                        Constants.userId + " INTEGER NOT NULL,"+

                        " FOREIGN KEY ("+ Constants.CAT+")"+
                        " REFERENCES " + Constants.IMG_CAT_TABLE_NAME + "(" + Constants.CAT +"), "+
                        " FOREIGN KEY ("+ Constants.userId+ ") " +
                        " REFERENCES "+Constants.USER_TABLE_NAME+ " ("+Constants.userId+") );"

                ;
        db.execSQL(create_budget);

        String create_saving=
                "CREATE TABLE " + Constants.SAVINGS_TABLE_NAME+ " (" +
                        Constants.COLUMN_SAVINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        Constants.CAT + " CARCHAR(20), " +
                        Constants.COLUMN_SAVINGS_NAME + " VARCHAR(20) NOT NULL, " +
                        Constants.COLUMN_TARGET_AMOUNT +  " DECIMAL(10,2), " +
                        Constants.COLUMN_TARGET_DATE + " DATE, " +
                        Constants.COLUMN_SAVINGS_NOTE + " VARCHAR(140)," +
                        Constants.COLUMN_SAVINGS_STATUS+" INT ,"+
                        Constants.userId+" INTEGER NOT NULL,"+

                        " FOREIGN KEY (" + Constants.CAT+")" +
                        " REFERENCES " + Constants.IMG_CAT_TABLE_NAME + "(" + Constants.CAT +") ,"+
                        " FOREIGN KEY ("+ Constants.userId+ ") " +
                        " REFERENCES "+Constants.USER_TABLE_NAME+ " ("+Constants.userId+") );"
                ;
        db.execSQL(create_saving);

        //Insert default images into the database
        for (String category:Constants.budgetImgNames) {
            int img = context.getResources().getIdentifier(category,"drawable", context.getPackageName());
            ContentValues values = new ContentValues();
            values.put("Category", category);
            values.put("Img", img);
            db.insert("ImgCategoryTable", null, values);
        }

        //Insert default images into the database
        for (String category:Constants.goalImgNames) {
            int img = context.getResources().getIdentifier(category,"drawable", context.getPackageName());
            ContentValues values = new ContentValues();
            values.put("Category", category);
            values.put("Img", img);
            db.insert("ImgCategoryTable", null, values);
        }

        //Insert default images into the database
        for (String category:Constants.defaultImgNames) {
            int img = context.getResources().getIdentifier(category,"drawable", context.getPackageName());
            ContentValues values = new ContentValues();
            values.put("Category", category);
            values.put("Img", img);
            db.insert("ImgCategoryTable", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TRANSACTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.BUDGET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.SAVINGS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.IMG_CAT_TABLE_NAME);
        onCreate(db);
    }


}
