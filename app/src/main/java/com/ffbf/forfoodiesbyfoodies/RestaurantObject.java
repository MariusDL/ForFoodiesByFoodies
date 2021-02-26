package com.ffbf.forfoodiesbyfoodies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//parcelable has been implemented to send restaurant objects between activities
public class RestaurantObject implements Parcelable {

    private int total_rating = 0;
    private int rating_number = 0;
    private String r_name;
    private String r_about;
    private String r_address;
    private String r_location;
    private String img = "";
    private String r_type;
    private ArrayList<String> r_reviews;


    public RestaurantObject(){}

    public RestaurantObject(String r_name, String r_type, String r_about,
                            String r_address, String r_location) {
        this.r_name = r_name;
        this.r_type = r_type;
        this.r_about = r_about;
        this.r_address = r_address;
        this.r_location = r_location;
        r_reviews = new ArrayList<>();
    }

    protected RestaurantObject(Parcel in){
        r_name = in.readString();
        r_type = in.readString();
        r_address = in.readString();
        r_location = in.readString();
        r_about = in.readString();
        img = in.readString();
        total_rating = in.readInt();
        rating_number = in.readInt();
    }
    public static final Creator<RestaurantObject> CREATOR = new Creator<RestaurantObject>() {
        @Override
        public RestaurantObject createFromParcel(Parcel parcel) {
            return new RestaurantObject(parcel);
        }
        @Override
        public RestaurantObject[] newArray(int i) {
            return new RestaurantObject[i];
        }
    };
    public String getR_location() {
        return r_location;
    }
    public void setR_location(String r_location) {
        this.r_location = r_location;
    }
    public String getR_address() {
        return r_address;
    }
    public void setR_address(String r_address) {
        this.r_address = r_address;
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
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public ArrayList<String> getR_reviews() {
        return r_reviews;
    }
    public void setR_reviews(ArrayList<String> r_reviews) {
        this.r_reviews = r_reviews;
    }
    public String getR_name() {
        return r_name;
    }
    public void setR_name(String r_name) {
        this.r_name = r_name;
    }
    public String getR_type() {
        return r_type;
    }
    public void setR_type(String r_type) {
        this.r_type = r_type;
    }
    public String getR_about() {
        return r_about;
    }
    public void setR_about(String r_about) {
        this.r_about = r_about;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(r_name);
        parcel.writeString(r_type);
        parcel.writeString(r_address);
        parcel.writeString(r_location);
        parcel.writeString(r_about);
        parcel.writeString(img);
        parcel.writeInt(total_rating);
        parcel.writeInt(rating_number);
    }
}
