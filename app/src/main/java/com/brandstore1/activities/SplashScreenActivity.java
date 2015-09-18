package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.brandstore1.R;
import com.brandstore1.model.Connection;
import com.brandstore1.utils.Connections;
import com.brandstore1.utils.MySharedPreferences;

public class SplashScreenActivity extends ActionBarActivity {


    Context mContext;

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
            // Check/Update GCM regid (if needed)

            // Execute some code after 2 seconds have passed
            // TODO: remove delay
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Connections.setUserIdFromSharedPreferences(mContext);
                    goToMainActivityScreen();
                    SplashScreenActivity.this.finish();
                }
            }, 3000);
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

}
