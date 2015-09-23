package com.brandstore1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;

/**
 * Created by I076324 on 7/22/2015.
 */
public class MySQLiteDatabase extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Brandstore.db";

    public MySQLiteDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void fillSuggestionTable(){
        //UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        //updateSuggestionsAsyncTask.execute();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
