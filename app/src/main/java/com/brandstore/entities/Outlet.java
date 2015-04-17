package com.brandstore.entities;

/**
 * Created by Ravi on 29-Mar-15.
 */
public class Outlet {


    private String BrandOutletName;
    private String ImageUrl;
    private String ContactNumber;
    private String FloorNumber;
    private String MallName;
    private String Price;
    private String Id;

    public String getGenderCodeString() {
        return genderCodeString;
    }

    public void setGenderCodeString(String genderCodeString) {
        this.genderCodeString = genderCodeString;
    }

    private String genderCodeString;

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



}
