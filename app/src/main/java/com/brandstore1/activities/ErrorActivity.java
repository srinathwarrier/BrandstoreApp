package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.brandstore1.BrandstoreApplication;
import com.brandstore1.R;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.gcm.GCMConnection;
import com.brandstore1.interfaces.UpdateSuggestionsAsyncResponse;
import com.brandstore1.utils.Connections;
import com.brandstore1.utils.MySharedPreferences;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ErrorActivity extends ActionBarActivity{

    private static final String TAG = "ErrorActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        mContext = this;

        // Get tracker.
        Tracker t = ((BrandstoreApplication) getApplication()).getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        // Send a screen view.
        t.setScreenName(TAG);
        t.send(new HitBuilders.ScreenViewBuilder().build());


    }

}
