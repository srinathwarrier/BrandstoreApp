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
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE_SUGGESTION =
            "CREATE TABLE " + MySqliteDatabaseContract.TableSuggestion.TABLE_NAME + " (" +
                    MySqliteDatabaseContract.TableSuggestion._ID + " INTEGER PRIMARY KEY," +
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_TITLE+ TEXT_TYPE + COMMA_SEP +
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_CATEGORY+ TEXT_TYPE + COMMA_SEP +
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_FLOOR_NAME+ TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MySqliteDatabaseContract.TableSuggestion.TABLE_NAME;

    private static final String EMPTY_SUGGESTION =
            "DELETE FROM " + MySqliteDatabaseContract.TableSuggestion.TABLE_NAME;


    public MySQLiteDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void fillSuggestionTable(){
        //UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        //updateSuggestionsAsyncTask.execute();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SUGGESTION);
    }

    public static void emptySuggestionTable(SQLiteDatabase db){
        db.execSQL(EMPTY_SUGGESTION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
