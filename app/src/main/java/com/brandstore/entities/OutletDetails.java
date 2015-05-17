package com.brandstore.entities;

import java.util.ArrayList;

/**
 * Created by Ravi on 30-Mar-15.
 */
public class OutletDetails {

    private String OutletName;
    private String OutletImage;
    private String Floor;
    private String HubName;
    private String Website;

    public String getGenderCodeString() {
        return GenderCodeString;
    }

    public void setGenderCodeString(String genderCodeString) {
        GenderCodeString = genderCodeString;
    }

    private String GenderCodeString;
    public ArrayList<String> getTag() {
        return Tag;
    }

    public void setTag(ArrayList<String> tag) {
        Tag = tag;
    }

    public ArrayList<String> getPrice() {
        return Price;
    }

    public void setPrice(ArrayList<String> price) {
        Price = price;
    }

    private ArrayList<String>Tag;
    private ArrayList<String>Price;
    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    private String ShortDescription;
    private String LongDescription;
   // private ArrayList<TagPrice> tagPriceArrayList;


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


   /* public ArrayList<TagPrice> getTagPriceArrayList() {
        return tagPriceArrayList;
    }

    public void setTagPriceArrayList(ArrayList<TagPrice> tagPriceArrayList) {
        this.tagPriceArrayList = tagPriceArrayList;
    }*/
}
