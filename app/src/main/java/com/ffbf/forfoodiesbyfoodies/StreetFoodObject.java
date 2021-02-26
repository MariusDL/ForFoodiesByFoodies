package com.ffbf.forfoodiesbyfoodies;


import android.os.Parcel;
import android.os.Parcelable;

public class StreetFoodObject implements Parcelable {

    private String sf_name;
    private String sf_description;
    private String img;
    private String sf_address;
    private String sf_location;
    private String sf_specific;
    private String sf_u_id; //id of the user that will add the street food location
    private int total_rating =0;
    private int rating_number =0;

    public StreetFoodObject(){}

    public StreetFoodObject(String sf_name, String sf_description, String sf_address, String sf_location, String sf_specific, String sf_u_id) {
        this.sf_name = sf_name;
        this.sf_description = sf_description;
        this.sf_address = sf_address;
        this.sf_location = sf_location;
        this.sf_specific = sf_specific;
        this.sf_u_id = sf_u_id;

    }

    protected StreetFoodObject(Parcel in){
        sf_name = in.readString();
        sf_description = in.readString();
        sf_address = in.readString();
        sf_location = in.readString();
        sf_specific = in.readString();
        sf_u_id = in.readString();
        img = in.readString();
        total_rating = in.readInt();
        rating_number = in.readInt();
    }

    public static final Creator<StreetFoodObject> CREATOR = new Creator<StreetFoodObject>() {
        @Override
        public StreetFoodObject createFromParcel(Parcel parcel) {
            return new StreetFoodObject(parcel);
        }

        @Override
        public StreetFoodObject[] newArray(int i) {
            return new StreetFoodObject[i];
        }
    };

    public String getSf_u_id() {
        return sf_u_id;
    }

    public void setSf_u_id(String sf_u_id) {
        this.sf_u_id = sf_u_id;
    }

    public String getSf_specific() {
        return sf_specific;
    }

    public void setSf_specific(String sf_specific) {
        this.sf_specific = sf_specific;
    }

    public String getSf_name() {
        return sf_name;
    }

    public void setSf_name(String sf_name) {
        this.sf_name = sf_name;
    }

    public String getSf_description() {
        return sf_description;
    }

    public void setSf_description(String sf_description) {
        this.sf_description = sf_description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSf_address() {
        return sf_address;
    }

    public void setSf_address(String sf_address) {
        this.sf_address = sf_address;
    }

    public String getSf_location() {
        return sf_location;
    }

    public void setSf_location(String sf_location) {
        this.sf_location = sf_location;
    }

    public int getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(int total_rating) {
        this.total_rating = total_rating;
    }

    public int getRating_number() {
        return rating_number;
    }

    public void setRating_number(int rating_number) {
        this.rating_number = rating_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(sf_name);
        parcel.writeString(sf_description);
        parcel.writeString(sf_address);
        parcel.writeString(sf_location);
        parcel.writeString(sf_specific);
        parcel.writeString(sf_u_id);
        parcel.writeString(img);
        parcel.writeInt(total_rating);
        parcel.writeInt(rating_number);
    }
}
