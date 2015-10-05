package com.brandstore1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.brandstore1.BrandstoreApplication;
import com.brandstore1.activities.MainActivity;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.entities.User;
import com.brandstore1.gcm.GCMConnection;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by I076324 on 7/22/2015.
 */
public class Connections {
    // SET THIS BOOLEAN VARIABLE TO FALSE FOR TESTING
    public static boolean isLiveSystem=true;

    public static String ipAddress  = "ec2-52-26-206-185.us-west-2.compute.amazonaws.com";
    private String systemName="brandstore";
    String versionName="v2";
    static String userId ="6";
    public Connections(){
        if(!isLiveSystem){
            setSystemName("beta");
        }

    }
    public enum AccountType{
        BRANDSTORE_ACCOUNT ,GOOGLE_ACCOUNT,FACEBOOK_ACCOUNT
    }

    /*
        Update suggestions in SQLite
     */
    public static void updateSuggestionInSQLite(Context mContext) {
        SQLiteDatabase sqLiteDatabase = mContext.openOrCreateDatabase("brandstoreDB", mContext.MODE_PRIVATE, null);
        UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        updateSuggestionsAsyncTask.execute();
    }

    public static void updateInstanceID( Context mContext) {
        GCMConnection.updateInstanceIDIfNeeded("Connections", mContext);
    }


    public static void fetchAndSaveUserObjectToSharedPreferences(JSONObject jsonObject, Context mContext){
        try{
            // Fetch jsonObject , create user object
            User user = new User();
            user.setUserId(jsonObject.getString("userID"));
            user.setName(jsonObject.getString("name"));
            user.setEmailId(jsonObject.getString("emailid"));

            // Convert user object to json object
            Gson gson = new Gson();
            String userJsonObject = gson.toJson(user);

            // Save to Shared Preferences
            MySharedPreferences.setUserId(mContext, user.getUserId());
            MySharedPreferences.setUserJsonObjectString(mContext, userJsonObject);
            MySharedPreferences.setHasLoggedIn(mContext, true);

            setUserIdFromSharedPreferences(mContext);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void closeScreenAndGoToMainActivityScreen(Context mContext){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }


    public static void performInitialLoginFormalities(JSONObject jsonObject, Context mContext){
        fetchAndSaveUserObjectToSharedPreferences(jsonObject,mContext);
        updateSuggestionInSQLite(mContext);
        updateInstanceID(mContext);
        closeScreenAndGoToMainActivityScreen(mContext);
    }
    public static void performInitialSignupFormalities(JSONObject jsonObject, Context mContext){
        fetchAndSaveUserObjectToSharedPreferences(jsonObject, mContext);
        updateSuggestionInSQLite(mContext);
        updateInstanceID(mContext);
        closeScreenAndGoToMainActivityScreen(mContext);
    }
    public static void performInitialOpeningAppFormalities( Context mContext){
        setUserIdFromSharedPreferences(mContext); // only userId variable needs to be set when opening app
        updateSuggestionInSQLite(mContext);
        updateInstanceID(mContext);
        closeScreenAndGoToMainActivityScreen(mContext);
    }



    public static void setUserIdFromSharedPreferences(Context mContext){
        //fetch userId from SharedPreferences
        userId = MySharedPreferences.getUserId(mContext);

        // You only need to set User ID on a tracker once. By setting it on the tracker, the ID will be
        // sent with all subsequent hits.
        Tracker t = ((BrandstoreApplication) ((Activity)mContext).getApplication()).getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        t.set("&uid", userId);

    }

    public String getLoginURL(String emailId , String password){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURLForSailsModel() +"user",
                    "emailid="+emailId+"&password="+password,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getForgotPasswordURL(String emailId ){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"forgotPassword",
                    "emailid="+emailId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getSignUpURL(String name , String emailId , String password ,String genderCode,String dobString){
        //http://localhost:8081/v2/signup?name=test6&emailid=test6@gmail.com&password=password8&gendercode=M&dob=1990-12-14
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"signup",
                    "name="+name+"&emailid="+emailId+"&password="+password +"&gendercode="+genderCode+"&dob="+dobString,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getExternalAccountLoginOrSignUpURL(String name , String accountId , String emailId ,String genderCode,String dobString , AccountType accountType){
        //http://localhost:8081/v2/signup?name=test6&emailid=test6@gmail.com&password=password8&gendercode=M&dob=1990-12-14
        String request="";
        String accountTypeString = getStringForAccountType(accountType);
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"externalAccountLoginOrSignUp",
                    "name="+name+"&accountid="+accountId+"&emailid="+emailId+"&gendercode="+genderCode+"&dob="+dobString+"&accounttype="+accountTypeString,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getStringForAccountType(AccountType accountType){
        String returnValue="brandstore";
        switch (accountType){
            case BRANDSTORE_ACCOUNT:
                returnValue = "brandstore";
                break;
            case GOOGLE_ACCOUNT:
                returnValue = "google";
                break ;
            case FACEBOOK_ACCOUNT:
                returnValue = "facebook";
                break ;
            default:
                returnValue = "brandstore";
        }
        return returnValue;
    }

    public String getUpdateRegIdURL(String registrationId){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"updateRegID",
                    "userid="+userId+"&regid="+registrationId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getSuggestionsURL(String query ){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getSuggestions",
                    "q="+query+"&userid="+userId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getOutletListForTagURL(String tagId ){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getOutlets",
                    "type=tag&userid="+userId+"&tagid="+tagId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getOutletListForCollectionURL(String collectionId ){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getOutletsForCollectionId",
                    "userid="+userId+"&collectionid="+collectionId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }




    public String getAllFavoriteOutletsURL(){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getAllFavoriteOutlets",
                    "userid="+userId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getAllOnSaleOutletsURL(){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getAllOnSaleOutlets",
                    "userid="+userId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }


    public String getOutletDetailsURL( String outletId ){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getOutletDetails",
                    "userid="+userId+"&id="+outletId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }


    public String getSetFavoriteOutletURL( String outletId , boolean toBeSet){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"setFavoriteOutlet",
                    "userid="+userId+"&outletid="+outletId+"&set="+toBeSet,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getTakeMeThereURL(String fromOutletId , String toOutletId  ){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL() +"getTakeMeThereCommands",
                    "userid="+userId+"&fromoutletid="+fromOutletId+"&tooutletid="+toOutletId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }









    public String getStartParametersOfURL(){
        if(isLiveSystem){
            return "/"+versionName+"/";
        }
        return "/"+ getSystemName() +"/"+versionName+"/";
    }
    public String getStartParametersOfURLForSailsModel(){
        if(isLiveSystem){
            return "/";
        }
        return "/"+ getSystemName() +"/";
    }


    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }


    public String getSystemNameCamelCase() {
        if(systemName.equals("beta")) return "Beta";
        else return "Brandstore";
    }


}
