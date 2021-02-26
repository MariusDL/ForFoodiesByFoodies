package com.ffbf.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewAddNew extends AppCompatActivity {


    private RatingBar ratingBar;
    private Button add_review;
    private EditText review_text;
    private TextView location;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferenceLocation;
    private FirebaseDatabase mDatabase;
    private Intent intent;
    private Bundle extras;
    private String location_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add_new);
        getSupportActionBar().hide();

        intent = getIntent();
        extras = intent.getParcelableExtra("extra");

        review_text = findViewById(R.id.new_review_text);
        location = findViewById(R.id.new_review_location);
        ratingBar = findViewById(R.id.bew_review_stars);
        add_review = findViewById(R.id.new_review_add_review);


        if(extras.getString("type").equals("StreetFood")){
            location_name = extras.getString("name");
            location.setText(location_name);
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference().child("street_food_reviews");
            mDatabaseReferenceLocation = mDatabase.getReference().child("street_food").child(location_name);
        } else {
            location_name = extras.getString("name");
            location.setText(location_name);
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference().child("restaurant_reviews");
            mDatabaseReferenceLocation = mDatabase.getReference().child("restaurants").child(location_name);
        }

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_review();
            }
        });

    }

    private void post_review(){
        final String rText = review_text.getText().toString();
        int stars =(int) ratingBar.getRating();
        final String locName = location_name;

        final String user_email = extras.getString("email");
        final String id = extras.getString("id");
        final int rating = extras.getInt("rating") +stars;
        final int number_of_ratings = extras.getInt("reviews_no") + 1;

        if(!TextUtils.isEmpty(rText)){
            ReviewObject review = new ReviewObject(locName,id,stars,rText, user_email);
            mDatabaseReference.child(locName).child(id).setValue(review);
            mDatabaseReferenceLocation.child("rating_number").setValue(number_of_ratings);
            mDatabaseReferenceLocation.child("total_rating").setValue(rating);

            Toast.makeText(ReviewAddNew.this,"Review added", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(ReviewAddNew.this,"Text box can not be empty", Toast.LENGTH_LONG).show();
        }
    }
}