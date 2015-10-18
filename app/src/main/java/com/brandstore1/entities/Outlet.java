package com.brandstore1.entities;

/**
 * Created by Ravi on 29-Mar-15.
 */
public class Outlet {


    private String BrandOutletName;
    private String ImageUrl;
    private String ContactNumber;
    private String FloorNumber;
    private String MallName;
    private String RelevantTag;
    private String Price;
    private String Id;
    private String genderCodeString;
    private String descriptionString;
    private String websiteString;
    private String isFavorite;
    private String isOnSale;

    public String getGenderCodeString() {
        return genderCodeString;
    }

    public void setGenderCodeString(String genderCodeString) {
        this.genderCodeString = genderCodeString;
    }

    public String getBrandOutletName() {
        return BrandOutletName;
    }

    public void setBrandOutletName(String brandOutletName) {
        BrandOutletName = brandOutletName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }


    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getFloorNumber() {
        return FloorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        FloorNumber = floorNumber;
    }

    public String getMallName() {
        return MallName;
    }

    public void setMallName(String mallName) {
        MallName = mallName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public String getRelevantTag() {
        return RelevantTag;
    }

    public void setRelevantTag(String relevantTag) {
        RelevantTag = relevantTag;
    }

    public String getDescriptionString() {
        return descriptionString;
    }

    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    public String getWebsiteString() {
        return websiteString;
    }

    public void setWebsiteString(String websiteString) {
        this.websiteString = websiteString;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(String isOnSale) {
        this.isOnSale = isOnSale;
    }


}
