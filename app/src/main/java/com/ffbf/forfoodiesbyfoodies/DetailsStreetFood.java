package com.ffbf.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsStreetFood extends AppCompatActivity {

    private ImageView street_food_image;
    private RatingBar street_food_ratingBar;
    private TextView street_food_ratings_number;
    private TextView street_food_name;
    private TextView street_food_specific;
    private TextView street_food_location;
    private TextView street_food_address;
    private TextView street_food_description;
    private Button street_food_reviews;
    private Button street_food_add_review;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private String userEmail;

    private StreetFoodObject streetFoodObject;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_street_food);
        getSupportActionBar().hide();

        final Intent i = getIntent();

        street_food_image = findViewById(R.id.street_food_details_image);
        street_food_ratings_number = findViewById(R.id.street_food_details_rating_number);
        street_food_ratingBar = findViewById(R.id.street_food_details_rating_bar);
        street_food_name = findViewById(R.id.street_food_details_name);
        street_food_specific = findViewById(R.id.street_food_details_specific);
        street_food_location = findViewById(R.id.street_food_details_location);
        street_food_address = findViewById(R.id.street_food_details_address);
        street_food_description = findViewById(R.id.street_food_details_description);
        street_food_reviews = findViewById(R.id.street_food_details_see_reviews);
        street_food_add_review = findViewById(R.id.street_food_details_add_review);
        street_food_description.setMovementMethod(new ScrollingMovementMethod());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference("streetFoodImages");
        mDatabase = FirebaseDatabase.getInstance();
        id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        userEmail = mUser.getEmail();
        streetFoodObject = i.getParcelableExtra("street_food");

        street_food_add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsStreetFood.this, ReviewAddNew.class);
                Bundle extras = new Bundle();
                extras.putString("type", "StreetFood");
                extras.putString("name",streetFoodObject.getSf_name());
                extras.putString("id", id);
                extras.putString("email", userEmail);
                extras.putInt("rating", streetFoodObject.getTotal_rating());
                extras.putInt("reviews_no", streetFoodObject.getRating_number());
                intent.putExtra("extra", extras);
                startActivity(intent);
            }
        });

        street_food_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsStreetFood.this, ReviewsList.class);
                intent.putExtra("name", streetFoodObject.getSf_name());
                intent.putExtra("type", "street_food");
                startActivity(intent);
            }
        });
        get_street_food_data();
    }
    private void get_street_food_data(){
        if(!streetFoodObject.getImg().equals("")){
            Picasso.get().load(streetFoodObject.getImg()).into(street_food_image);
        }
        street_food_name.setText(streetFoodObject.getSf_name());
        street_food_specific.setText(streetFoodObject.getSf_specific());
        street_food_address.setText("Address: "+ streetFoodObject.getSf_address());
        street_food_description.setText(streetFoodObject.getSf_description());
        street_food_location.setText("Location: " + streetFoodObject.getSf_location());
        street_food_ratings_number.setText("("+streetFoodObject.getRating_number()+")");
        float rate;
        if(streetFoodObject.getRating_number()>0){
            rate = streetFoodObject.getTotal_rating()/streetFoodObject.getRating_number();
        }else {
            rate =0;
        }
        street_food_ratingBar.setRating(rate);
    }

}