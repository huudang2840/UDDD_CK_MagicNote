package com.example.magicnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public  static final String DBNAME = "login.db";


    public DBHelper(@Nullable Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(email TEXT primary key, password TEXT, fullName TEXT, age TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");

    }
    public Boolean insertData(String email, String password, String fullName, String age){
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("password", password);
        values.put("fullName", fullName);
        values.put("age", age);

        long result= db.insert( "users", null, values);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

}
