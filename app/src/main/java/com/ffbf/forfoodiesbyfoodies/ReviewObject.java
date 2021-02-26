package com.ffbf.forfoodiesbyfoodies;

import java.util.HashMap;


public class ReviewObject {
    private String location_name;
    private String user_id;
    private String review_text;
    private String user_email;
    private int stars=0;
    HashMap<String, String> votes = new HashMap<>();

    private ReviewObject(){}

    public ReviewObject(String location_name, String user_id, int stars, String review_text, String user_email) {
        this.location_name = location_name;
        this.user_id = user_id;
        this.stars = stars;
        this.review_text = review_text;
        this.user_email = user_email;
        votes.put(user_id,"in");
    }

    public HashMap<String, String> getVotes() {
        return votes;
    }

    public void setVotes(HashMap<String, String> votes) {
        this.votes = votes;
    }

    public String getUserEmail() {
        return user_email;
    }

    public void setUserEmail(String user_email) {
        this.user_email = user_email;
    }

    public String getReviewText() {
        return review_text;
    }

    public void setReviewText(String review_text) {
        this.review_text = review_text;
    }

    public String getRestaurantName() {
        return location_name;
    }

    public void setRestaurantName(String location_name) {
        this.location_name = location_name;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public int getRestaurantGivenStars() {
        return stars;
    }

    public void setRestaurantGivenStars(int stars) {
        this.stars = stars;
    }

}