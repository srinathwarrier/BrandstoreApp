package com.brandstore1.gcm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by I076324 on 10/1/2015.
 */
public class GCMConnection {

    // GCM Variables
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(String TAG, Context mContext) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        try{
            Activity activity = (Activity)mContext;
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                            .show();
                } else {
                    Log.i(TAG, "This device is not supported.");
                    activity.finish();
                }
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static void updateInstanceID(String TAG, Context mContext){
        if (checkPlayServices(TAG, mContext)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(mContext, RegistrationIntentService.class);
            mContext.startService(intent);
        }
    }

    public static void updateInstanceIDIfNeeded(String TAG, Context mContext){
        //TODO: Check if token needs to be updated.
        boolean isUpdatedRequired=true;
        // Currently, token is updated every time user opens app.
        if(isUpdatedRequired){
            updateInstanceID(TAG,mContext);
        }
    }
}
