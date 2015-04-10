package com.brandstore.entities;

/**
 * Created by Ravi on 30-Mar-15.
 */
public class OutletDetails {


    public String getOutletImage() {
        return OutletImage;
    }

    public void setOutletImage(String outletImage) {
        OutletImage = outletImage;
    }

    public String getOutletName() {
        return OutletName;
    }

    public void setOutletName(String outletName) {
        OutletName = outletName;
    }


    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getHubName() {
        return HubName;
    }

    public void setHubName(String hubName) {
        HubName = hubName;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    private String OutletImage;
    private String OutletName;
    private String Floor;
    private String HubName;
    private String Website;
    private String Description;

}
