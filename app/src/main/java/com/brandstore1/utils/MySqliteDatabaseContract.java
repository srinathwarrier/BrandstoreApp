package com.brandstore1.utils;

import android.provider.BaseColumns;

/**
 * Created by I076324 on 9/20/2015.
 */
public final class MySqliteDatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MySqliteDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TableSuggestion implements BaseColumns {
        public static final String TABLE_NAME = "suggestion";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}