package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.brandstore1.R;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.gcm.RegistrationIntentService;
import com.brandstore1.interfaces.UpdateSuggestionsAsyncResponse;
import com.brandstore1.model.Connection;
import com.brandstore1.utils.Connections;
import com.brandstore1.utils.MySharedPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class SplashScreenActivity extends ActionBarActivity implements UpdateSuggestionsAsyncResponse{


    private static final String TAG = "MainActivity";
    Context mContext;
    SQLiteDatabase sqLiteDatabase;
    // GCM Variables
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        //SharedPreferences sharedPref = mContext.getSharedPreferences("BrandstoreApp",Context.MODE_PRIVATE);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        //boolean hasLoggedIn = sharedPref.getBoolean("hasLoggedIn", false);

        boolean hasLoggedIn = MySharedPreferences.getHasLoggedIn(mContext);

        if(hasLoggedIn)
        {
            // TODO: Initial Tasks
            // Update SQLite database (if needed)
            sqLiteDatabase = openOrCreateDatabase("brandstoreDB",MODE_PRIVATE,null);
            UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
            updateSuggestionsAsyncTask.delegate=this;
            updateSuggestionsAsyncTask.execute();

            // Check/Update GCM regid (if needed)
            updateInstanceID();


            // Execute some code after 2 seconds have passed
            // TODO: remove delay
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Connections.setUserIdFromSharedPreferences(mContext);
                    goToMainActivityScreen();
                    SplashScreenActivity.this.finish();
                }
            }, 1000);

        }
        else{
            // Go directly to main activity.
            // TODO: remove delay
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    goToLoginActivityScreen();
                    SplashScreenActivity.this.finish();
                }
            }, 3000);
        }

    }

    /*
        Go To Screen methods
     */

    public void goToLoginActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void goToMainActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void closeAndGoToMainActivityScreen(){
        Connections.setUserIdFromSharedPreferences(mContext);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        SplashScreenActivity.this.finish();
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void updateInstanceID(){
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
}
