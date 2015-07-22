package com.brandstore1.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by I076324 on 7/22/2015.
 */
public class Connections {
    // SET THIS BOOLEAN VARIABLE TO FALSE FOR TESTING
    public static boolean isLiveSystem=true;

    //public String getSlateItemsURL = "https://slate-muzak.rhcloud.com/getSlateItems.php?id=";
    public static String ipAddress  = "ec2-52-26-206-185.us-west-2.compute.amazonaws.com";
    private String systemName="brandstore";
    String versionName="v2";
    public Connections(){
        if(!isLiveSystem){
            setSystemName("beta");
        }
    }

    public String getSlateItemsURL(String userId){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    getStartParametersOfURL()+"getRecentAndPopularSuggestions", //  /v2/getRecentAndPopularSuggestions
                    "id=" + userId,
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }


    public String getSystemNameCamelCase() {
        if(systemName.equals("beta")) return "Beta";
        else return "Slate";
    }
}
