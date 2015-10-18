package com.brandstore1.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by i076324 on 10/17/15.
 */
public class OutletListFilterConstraint {

    /* Gender Filter Constraints */
    private boolean HAS_TO_BE_MALE_OUTLET;
    private boolean HAS_TO_BE_FEMALE_OUTLET;
    private boolean HAS_TO_BE_CHILDREN_OUTLET;
    public final String FILTER_GENDER_MALE = "Male";
    public final String FILTER_GENDER_FEMALE = "Female";
    public final String FILTER_GENDER_CHILDREN ="Children";

    /* Average Price Filter Constraints */
    private boolean PRICE_BETWEEN_0_AND_500;
    private boolean PRICE_BETWEEN_500_AND_1500;
    private boolean PRICE_BETWEEN_1500_AND_2500;
    private boolean PRICE_BETWEEN_2500_AND_3500;
    private boolean PRICE_ABOVE_3500;
    public final String FILTER_PRICE_BETWEEN_0_AND_500 = "0-500";
    public final String FILTER_PRICE_BETWEEN_500_AND_1500 = "500-1500";
    public final String FILTER_PRICE_BETWEEN_1500_AND_2500 = "1500-2500";
    public final String FILTER_PRICE_BETWEEN_2500_AND_3500 = "2500-3500";
    public final String FILTER_PRICE_ABOVE_3500 = "3500+";


    /* Floor Constraints */
    private boolean HAS_TO_BE_LOWER_GROUND_FLOOR;
    private boolean HAS_TO_BE_GROUND_FLOOR;
    private boolean HAS_TO_BE_FIRST_FLOOR;
    private boolean HAS_TO_BE_SECOND_FLOOR;
    public final String FLOOR_LOWER_GROUND = "Lower ground floor";
    public final String FLOOR_GROUND = "Ground floor";
    public final String FLOOR_FIRST = "First floor";
    public final String FLOOR_SECOND = "Second floor";

    /* More Filter Constraints */
    private boolean HAS_TO_BE_ON_SALE;
    private boolean HAS_TO_BE_FAVORITE;
    public final String FILTER_ON_SALE= "On Sale";
    public final String FILTER_IS_FAVORITE = "My Favourites";

    /* Sort Types */
    private boolean SORT_BY_RELEVANCE=true;
    private boolean SORT_BY_PRICE_LOW_TO_HIGH;
    private boolean SORT_BY_PRICE_HIGH_TO_LOW;
    public final String STRING_SORT_BY_RELEVANCE= "Relevance";
    public final String STRING_SORT_BY_PRICE_LOW_TO_HIGH= "Average Price (Low to high)";
    public final String STRING_SORT_BY_PRICE_HIGH_TO_LOW= "Average Price (High to low)";


    // Create empty constraint ArrayLists
    public ArrayList<String> genderArrayList = new ArrayList<>();
    public ArrayList<String> averagePriceArrayList = new ArrayList<>();
    public ArrayList<String> floorArrayList = new ArrayList<>();
    public ArrayList<String> moreFiltersArrayList = new ArrayList<>();
    public ArrayList<String> sortArrayList = new ArrayList<>();

    public OutletListFilterConstraint(){
        genderArrayList.add(FILTER_GENDER_MALE);
        genderArrayList.add(FILTER_GENDER_FEMALE);
        genderArrayList.add(FILTER_GENDER_CHILDREN);
        averagePriceArrayList.add(FILTER_PRICE_BETWEEN_0_AND_500);
        averagePriceArrayList.add(FILTER_PRICE_BETWEEN_500_AND_1500);
        averagePriceArrayList.add(FILTER_PRICE_BETWEEN_1500_AND_2500);
        averagePriceArrayList.add(FILTER_PRICE_BETWEEN_2500_AND_3500);
        averagePriceArrayList.add(FILTER_PRICE_ABOVE_3500);
        moreFiltersArrayList.add(FILTER_ON_SALE);
        moreFiltersArrayList.add(FILTER_IS_FAVORITE);
        sortArrayList.add(STRING_SORT_BY_RELEVANCE);
        sortArrayList.add(STRING_SORT_BY_PRICE_LOW_TO_HIGH);
        sortArrayList.add(STRING_SORT_BY_PRICE_HIGH_TO_LOW);
        floorArrayList.add(FLOOR_LOWER_GROUND);
        floorArrayList.add(FLOOR_GROUND);
        floorArrayList.add(FLOOR_FIRST);
        floorArrayList.add(FLOOR_SECOND);
    }

    public boolean satisfiesConstraint(Outlet outlet){
        boolean returnValue = true;
        if(isHAS_TO_BE_FAVORITE()){
            if(!outlet.getIsFavorite().equals("true")) {
                return false;
            }
            returnValue = true;
        }
        if(isHAS_TO_BE_ON_SALE()){
            if(!outlet.getIsOnSale().equals("true")){
                return false;
            }
            returnValue = true;
        }
        // Look for all 3 checks : Male , Female, Children
        if(isHAS_TO_BE_MALE_OUTLET()){
            // If male is checked, look for "M" in genderCodeString
            if(!outlet.getGenderCodeString().contains("M")){
                return false;
            }
            returnValue = true;
        }
        if(isHAS_TO_BE_FEMALE_OUTLET()){
            // If female is checked, look for "F" in genderCodeString
            if(!outlet.getGenderCodeString().contains("F")){
                return false;
            }
            returnValue = true;
        }
        if(isHAS_TO_BE_CHILDREN_OUTLET()){
            // If children is checked, look for "K" in genderCodeString
            if(!outlet.getGenderCodeString().contains("K")){
                return false;
            }
            returnValue = true;
        }
        if(isPRICE_BETWEEN_0_AND_500() ||
                isPRICE_BETWEEN_500_AND_1500() ||
                isPRICE_BETWEEN_1500_AND_2500() ||
                isPRICE_BETWEEN_2500_AND_3500() ||
                isPRICE_ABOVE_3500() )
        {
            if(outlet.getPrice().equals("")){
                return false;
            }
            int price = Integer.parseInt(outlet.getPrice());
            returnValue = returnValue &&
                    (    ( (price>=0 && price<=500)      &&  (isPRICE_BETWEEN_0_AND_500()) ) ||
                        ( (price>=500 && price<=1500)   &&  (isPRICE_BETWEEN_500_AND_1500()) ) ||
                        ( (price>=1500 && price<=2500)   &&  (isPRICE_BETWEEN_1500_AND_2500()) ) ||
                        ( (price>=2500 && price<=3500)   &&  (isPRICE_BETWEEN_2500_AND_3500()) ) ||
                        ( (price>=3500)   &&  (isPRICE_ABOVE_3500()) )
            );

        }
        if(isHAS_TO_BE_LOWER_GROUND_FLOOR() ||
                isHAS_TO_BE_GROUND_FLOOR()  ||
                isHAS_TO_BE_FIRST_FLOOR()   ||
                isHAS_TO_BE_SECOND_FLOOR()  )
        {
            returnValue = returnValue &&
                    (   (outlet.getFloorNumber().equalsIgnoreCase(FLOOR_LOWER_GROUND) && isHAS_TO_BE_LOWER_GROUND_FLOOR() ) ||
                        (outlet.getFloorNumber().equalsIgnoreCase(FLOOR_GROUND)       && isHAS_TO_BE_GROUND_FLOOR() ) ||
                        (outlet.getFloorNumber().equalsIgnoreCase(FLOOR_FIRST)        && isHAS_TO_BE_FIRST_FLOOR() ) ||
                        (outlet.getFloorNumber().equalsIgnoreCase(FLOOR_SECOND)       && isHAS_TO_BE_SECOND_FLOOR() )
                    );
        }
        return returnValue;
    }

    public ArrayList<Outlet> getSortedOutletArrayList(ArrayList<Outlet> outletArrayList){
        if(isSORT_BY_PRICE_LOW_TO_HIGH()){
            Collections.sort(outletArrayList, new Comparator<Outlet>() {
                @Override
                public int compare(Outlet outlet1, Outlet outlet2) {
                    int price1 = (outlet1.getPrice().equals(""))?0:Integer.parseInt(outlet1.getPrice()),
                            price2 = (outlet2.getPrice().equals(""))?0:Integer.parseInt(outlet2.getPrice());
                    return (price1 - price2);
                }
            });
            return outletArrayList;
        }else if(isSORT_BY_PRICE_HIGH_TO_LOW()){
            Collections.sort(outletArrayList, new Comparator<Outlet>() {
                @Override
                public int compare(Outlet outlet1, Outlet outlet2) {
                    int price1 = (outlet1.getPrice().equals(""))?0:Integer.parseInt(outlet1.getPrice()),
                            price2 = (outlet2.getPrice().equals(""))?0:Integer.parseInt(outlet2.getPrice());
                    return (price2 - price1);
                }
            });
            return outletArrayList;
        }
        return outletArrayList;
    }

    public boolean getIsClicked(String FILTER_TITLE){
        switch (FILTER_TITLE){
            case FILTER_IS_FAVORITE:
                return isHAS_TO_BE_FAVORITE();
            case FILTER_ON_SALE:
                return isHAS_TO_BE_ON_SALE();
            case FILTER_GENDER_MALE:
                return isHAS_TO_BE_MALE_OUTLET();
            case FILTER_GENDER_FEMALE:
                return isHAS_TO_BE_FEMALE_OUTLET();
            case FILTER_GENDER_CHILDREN:
                return isHAS_TO_BE_CHILDREN_OUTLET();
            case FILTER_PRICE_BETWEEN_0_AND_500 :
                return isPRICE_BETWEEN_0_AND_500();
            case FILTER_PRICE_BETWEEN_500_AND_1500 :
                return isPRICE_BETWEEN_500_AND_1500();
            case FILTER_PRICE_BETWEEN_1500_AND_2500 :
                return isPRICE_BETWEEN_1500_AND_2500();
            case FILTER_PRICE_BETWEEN_2500_AND_3500 :
                return isPRICE_BETWEEN_2500_AND_3500();
            case FILTER_PRICE_ABOVE_3500 :
                return isPRICE_ABOVE_3500();
            case STRING_SORT_BY_RELEVANCE:
                return isSORT_BY_RELEVANCE();
            case STRING_SORT_BY_PRICE_LOW_TO_HIGH:
                return isSORT_BY_PRICE_LOW_TO_HIGH();
            case STRING_SORT_BY_PRICE_HIGH_TO_LOW:
                return isSORT_BY_PRICE_HIGH_TO_LOW();
            case FLOOR_LOWER_GROUND:
                return isHAS_TO_BE_LOWER_GROUND_FLOOR();
            case FLOOR_GROUND:
                return isHAS_TO_BE_GROUND_FLOOR();
            case FLOOR_FIRST:
                return isHAS_TO_BE_FIRST_FLOOR();
            case FLOOR_SECOND:
                return isHAS_TO_BE_SECOND_FLOOR();
        }
        return false;
    }

    public void onFilterClicked(String FILTER_TITLE, boolean isSetNow){
        switch (FILTER_TITLE){
            case FILTER_IS_FAVORITE:
                setHAS_TO_BE_FAVORITE(isSetNow);
                break;
            case FILTER_ON_SALE:
                setHAS_TO_BE_ON_SALE(isSetNow);
                break;
            case FILTER_GENDER_MALE:
                setHAS_TO_BE_MALE_OUTLET(isSetNow);
                setHAS_TO_BE_FEMALE_OUTLET(false);
                setHAS_TO_BE_CHILDREN_OUTLET(false);
                break;
            case FILTER_GENDER_FEMALE:
                setHAS_TO_BE_MALE_OUTLET(false);
                setHAS_TO_BE_FEMALE_OUTLET(isSetNow);
                setHAS_TO_BE_CHILDREN_OUTLET(false);
                break;
            case FILTER_GENDER_CHILDREN:
                setHAS_TO_BE_MALE_OUTLET(false);
                setHAS_TO_BE_FEMALE_OUTLET(false);
                setHAS_TO_BE_CHILDREN_OUTLET(isSetNow);
                break;
            case FILTER_PRICE_BETWEEN_0_AND_500 :
                setPRICE_BETWEEN_0_AND_500(isSetNow);
                break;
            case FILTER_PRICE_BETWEEN_500_AND_1500 :
                setPRICE_BETWEEN_500_AND_1500(isSetNow);
                break;
            case FILTER_PRICE_BETWEEN_1500_AND_2500 :
                setPRICE_BETWEEN_1500_AND_2500(isSetNow);
                break;
            case FILTER_PRICE_BETWEEN_2500_AND_3500 :
                setPRICE_BETWEEN_2500_AND_3500(isSetNow);
                break;
            case FILTER_PRICE_ABOVE_3500 :
                setPRICE_ABOVE_3500(isSetNow);
                break;
            case STRING_SORT_BY_RELEVANCE :
                setSORT_BY_RELEVANCE(true);
                setSORT_BY_PRICE_LOW_TO_HIGH(false);
                setSORT_BY_PRICE_HIGH_TO_LOW(false);
                break;
            case STRING_SORT_BY_PRICE_LOW_TO_HIGH:
                setSORT_BY_RELEVANCE(false);
                setSORT_BY_PRICE_LOW_TO_HIGH(true);
                setSORT_BY_PRICE_HIGH_TO_LOW(false);
                break;
            case STRING_SORT_BY_PRICE_HIGH_TO_LOW:
                setSORT_BY_RELEVANCE(false);
                setSORT_BY_PRICE_LOW_TO_HIGH(false);
                setSORT_BY_PRICE_HIGH_TO_LOW(true);
                break;
            case FLOOR_LOWER_GROUND:
                setHAS_TO_BE_LOWER_GROUND_FLOOR(isSetNow);
                break;
            case FLOOR_GROUND:
                setHAS_TO_BE_GROUND_FLOOR(isSetNow);
                break;
            case FLOOR_FIRST:
                setHAS_TO_BE_FIRST_FLOOR(isSetNow);
                break;
            case FLOOR_SECOND:
                setHAS_TO_BE_SECOND_FLOOR(isSetNow);
                break;
        }
    }

    public boolean isInResetState(){
        if( isSORT_BY_RELEVANCE() &&
                !isSORT_BY_PRICE_HIGH_TO_LOW() &&
                !isSORT_BY_PRICE_LOW_TO_HIGH() &&
                !isPRICE_BETWEEN_0_AND_500() &&
                !isPRICE_BETWEEN_500_AND_1500() &&
                !isPRICE_BETWEEN_1500_AND_2500() &&
                !isPRICE_BETWEEN_2500_AND_3500() &&
                !isPRICE_ABOVE_3500() &&
                !isHAS_TO_BE_MALE_OUTLET() &&
                !isHAS_TO_BE_FEMALE_OUTLET() &&
                !isHAS_TO_BE_CHILDREN_OUTLET() &&
                !isHAS_TO_BE_FAVORITE() &&
                !isHAS_TO_BE_ON_SALE() &&
                !isHAS_TO_BE_LOWER_GROUND_FLOOR() &&
                !isHAS_TO_BE_GROUND_FLOOR() &&
                !isHAS_TO_BE_FIRST_FLOOR() &&
                !isHAS_TO_BE_SECOND_FLOOR()
                ){
            return true;
        }
        return false;
    }
    public void setToResetState(){
        setSORT_BY_RELEVANCE(true);
        setSORT_BY_PRICE_HIGH_TO_LOW(false);
        setSORT_BY_PRICE_LOW_TO_HIGH(false);
        setPRICE_BETWEEN_0_AND_500(false);
        setPRICE_BETWEEN_500_AND_1500(false);
        setPRICE_BETWEEN_1500_AND_2500(false);
        setPRICE_BETWEEN_2500_AND_3500(false);
        setPRICE_ABOVE_3500(false);
        setHAS_TO_BE_MALE_OUTLET(false);
        setHAS_TO_BE_FEMALE_OUTLET(false);
        setHAS_TO_BE_CHILDREN_OUTLET(false);
        setHAS_TO_BE_FAVORITE(false);
        setHAS_TO_BE_ON_SALE(false);
        setHAS_TO_BE_LOWER_GROUND_FLOOR(false);
        setHAS_TO_BE_GROUND_FLOOR(false);
        setHAS_TO_BE_FIRST_FLOOR(false);
        setHAS_TO_BE_SECOND_FLOOR(false);
    }


    public boolean isHAS_TO_BE_MALE_OUTLET() {
        return HAS_TO_BE_MALE_OUTLET;
    }

    public void setHAS_TO_BE_MALE_OUTLET(boolean HAS_TO_BE_MALE_OUTLET) {
        this.HAS_TO_BE_MALE_OUTLET = HAS_TO_BE_MALE_OUTLET;
    }

    public boolean isHAS_TO_BE_FEMALE_OUTLET() {
        return HAS_TO_BE_FEMALE_OUTLET;
    }

    public void setHAS_TO_BE_FEMALE_OUTLET(boolean HAS_TO_BE_FEMALE_OUTLET) {
        this.HAS_TO_BE_FEMALE_OUTLET = HAS_TO_BE_FEMALE_OUTLET;
    }

    public boolean isHAS_TO_BE_CHILDREN_OUTLET() {
        return HAS_TO_BE_CHILDREN_OUTLET;
    }

    public void setHAS_TO_BE_CHILDREN_OUTLET(boolean HAS_TO_BE_CHILDREN_OUTLET) {
        this.HAS_TO_BE_CHILDREN_OUTLET = HAS_TO_BE_CHILDREN_OUTLET;
    }

    public boolean isPRICE_BETWEEN_0_AND_500() {
        return PRICE_BETWEEN_0_AND_500;
    }

    public void setPRICE_BETWEEN_0_AND_500(boolean PRICE_BETWEEN_0_AND_500) {
        this.PRICE_BETWEEN_0_AND_500 = PRICE_BETWEEN_0_AND_500;
    }

    public boolean isPRICE_BETWEEN_500_AND_1500() {
        return PRICE_BETWEEN_500_AND_1500;
    }

    public void setPRICE_BETWEEN_500_AND_1500(boolean PRICE_BETWEEN_500_AND_1500) {
        this.PRICE_BETWEEN_500_AND_1500 = PRICE_BETWEEN_500_AND_1500;
    }

    public boolean isPRICE_BETWEEN_1500_AND_2500() {
        return PRICE_BETWEEN_1500_AND_2500;
    }

    public void setPRICE_BETWEEN_1500_AND_2500(boolean PRICE_BETWEEN_1500_AND_2500) {
        this.PRICE_BETWEEN_1500_AND_2500 = PRICE_BETWEEN_1500_AND_2500;
    }

    public boolean isPRICE_BETWEEN_2500_AND_3500() {
        return PRICE_BETWEEN_2500_AND_3500;
    }

    public void setPRICE_BETWEEN_2500_AND_3500(boolean PRICE_BETWEEN_2500_AND_3500) {
        this.PRICE_BETWEEN_2500_AND_3500 = PRICE_BETWEEN_2500_AND_3500;
    }

    public boolean isHAS_TO_BE_ON_SALE() {
        return HAS_TO_BE_ON_SALE;
    }

    public void setHAS_TO_BE_ON_SALE(boolean HAS_TO_BE_ON_SALE) {
        this.HAS_TO_BE_ON_SALE = HAS_TO_BE_ON_SALE;
    }

    public boolean isHAS_TO_BE_FAVORITE() {
        return HAS_TO_BE_FAVORITE;
    }

    public void setHAS_TO_BE_FAVORITE(boolean HAS_TO_BE_FAVORITE) {
        this.HAS_TO_BE_FAVORITE = HAS_TO_BE_FAVORITE;
    }

    public boolean isPRICE_ABOVE_3500() {
        return PRICE_ABOVE_3500;
    }

    public void setPRICE_ABOVE_3500(boolean PRICE_ABOVE_3500) {
        this.PRICE_ABOVE_3500 = PRICE_ABOVE_3500;
    }

    public boolean isSORT_BY_RELEVANCE() {
        return SORT_BY_RELEVANCE;
    }

    public void setSORT_BY_RELEVANCE(boolean SORT_BY_RELEVANCE) {
        this.SORT_BY_RELEVANCE = SORT_BY_RELEVANCE;
    }

    public boolean isSORT_BY_PRICE_LOW_TO_HIGH() {
        return SORT_BY_PRICE_LOW_TO_HIGH;
    }

    public void setSORT_BY_PRICE_LOW_TO_HIGH(boolean SORT_BY_PRICE_LOW_TO_HIGH) {
        this.SORT_BY_PRICE_LOW_TO_HIGH = SORT_BY_PRICE_LOW_TO_HIGH;
    }

    public boolean isSORT_BY_PRICE_HIGH_TO_LOW() {
        return SORT_BY_PRICE_HIGH_TO_LOW;
    }

    public void setSORT_BY_PRICE_HIGH_TO_LOW(boolean SORT_BY_PRICE_HIGH_TO_LOW) {
        this.SORT_BY_PRICE_HIGH_TO_LOW = SORT_BY_PRICE_HIGH_TO_LOW;
    }

    public boolean isHAS_TO_BE_LOWER_GROUND_FLOOR() {
        return HAS_TO_BE_LOWER_GROUND_FLOOR;
    }

    public void setHAS_TO_BE_LOWER_GROUND_FLOOR(boolean HAS_TO_BE_LOWER_GROUND_FLOOR) {
        this.HAS_TO_BE_LOWER_GROUND_FLOOR = HAS_TO_BE_LOWER_GROUND_FLOOR;
    }

    public boolean isHAS_TO_BE_GROUND_FLOOR() {
        return HAS_TO_BE_GROUND_FLOOR;
    }

    public void setHAS_TO_BE_GROUND_FLOOR(boolean HAS_TO_BE_GROUND_FLOOR) {
        this.HAS_TO_BE_GROUND_FLOOR = HAS_TO_BE_GROUND_FLOOR;
    }

    public boolean isHAS_TO_BE_FIRST_FLOOR() {
        return HAS_TO_BE_FIRST_FLOOR;
    }

    public void setHAS_TO_BE_FIRST_FLOOR(boolean HAS_TO_BE_FIRST_FLOOR) {
        this.HAS_TO_BE_FIRST_FLOOR = HAS_TO_BE_FIRST_FLOOR;
    }

    public boolean isHAS_TO_BE_SECOND_FLOOR() {
        return HAS_TO_BE_SECOND_FLOOR;
    }

    public void setHAS_TO_BE_SECOND_FLOOR(boolean HAS_TO_BE_SECOND_FLOOR) {
        this.HAS_TO_BE_SECOND_FLOOR = HAS_TO_BE_SECOND_FLOOR;
    }
}
