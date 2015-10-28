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
    public abstract class TableSuggestion implements BaseColumns {
        public static final String TABLE_NAME = "Suggestion";
        public static final String _ID = "_ID";
        public static final String COLUMN_NAME_ID  = "id";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_FLOOR_NAME = "floor";

    }

    public abstract class TableRecentSuggestion implements BaseColumns {
        public static final String TABLE_NAME = "RecentSuggestion";
        public static final String _ID = "_ID";
        public static final String COLUMN_NAME_ID  = "id";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_FLOOR_NAME = "floor";

    }
    public abstract class TablePopularSuggestion implements BaseColumns {
        public static final String TABLE_NAME = "RecentSuggestion";
        public static final String _ID = "_ID";
        public static final String COLUMN_NAME_ID  = "id";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_FLOOR_NAME = "floor";

    }

}