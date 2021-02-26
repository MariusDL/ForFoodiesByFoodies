package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailsRestaurant extends AppCompatActivity {

    private StorageReference mStorage;
    private DatabaseReference mDatabaseReferenceUser, mDatabaseReferenceRestaurant;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private String email;
    private String critic = "false";

    private ImageView restaurant_image;
    private TextView restaurant_name;
    private TextView restaurant_specific;
    private TextView restaurant_address;
    private TextView restaurant_description;
    private TextView restaurant_reviews_number;
    private TextView restaurant_location;
    private RatingBar restaurant_rating;
    private Button see_restaurant_reviews;
    private Button add_restaurant_review;
    private Button restaurant_reservation;
    private RestaurantObject restaurant;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference("restaurant_images");
        mDatabase = FirebaseDatabase.getInstance();
        id = mAuth.getCurrentUser().getUid();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        email = mUser.getEmail();

        restaurant_reviews_number = findViewById(R.id.restaurant_details_rating_number);
        restaurant_rating = findViewById(R.id.restaurant_details_rating_bar);
        see_restaurant_reviews = findViewById(R.id.restaurant_details_see_reviews);
        add_restaurant_review = findViewById(R.id.restaurant_details_add_review);
        restaurant_reservation = findViewById(R.id.restaurant_details_make_reservation);
        restaurant_location = findViewById(R.id.restaurant_details_location);
        restaurant_image = findViewById(R.id.restaurant_details_image);
        restaurant_name = findViewById(R.id.restaurant_details_name);
        restaurant_specific = findViewById(R.id.restaurant_details_specific);
        restaurant_address = findViewById(R.id.restaurant_details_address);
        restaurant_description = findViewById(R.id.restaurant_details_description);
        restaurant_description.setMovementMethod(new ScrollingMovementMethod());

        add_restaurant_review.setEnabled(false);
        add_restaurant_review.setVisibility(View.INVISIBLE);

        final Intent intent = getIntent();
        restaurant = intent.getParcelableExtra("Restaurant");

        mDatabaseReferenceUser = mDatabase.getReference().child("users").child(id);
        mDatabaseReferenceRestaurant = mDatabase.getReference().child("restaurants").child(restaurant.getR_name());

        restaurant_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsRestaurant.this, RestaurantReservation.class);
                intent.putExtra("name", restaurant.getR_name());
                startActivity(intent);
            }
        });

        see_restaurant_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsRestaurant.this, ReviewsList.class);
                intent.putExtra("name", restaurant.getR_name());
                intent.putExtra("type", "Restaurant");
                startActivity(intent);
            }
        });

        add_restaurant_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent r_int = new Intent(DetailsRestaurant.this, ReviewAddNew.class);
                Bundle extras = new Bundle();
                extras.putString("type", "Restaurant");
                extras.putString("name",restaurant.getR_name());
                extras.putString("id", id);
                extras.putString("email", email);
                extras.putInt("rating", restaurant.getTotal_rating());
                extras.putInt("reviews_no", restaurant.getRating_number());
                r_int.putExtra("extra", extras);
                startActivity(r_int);

            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user!=null){
                    if(user.getCritic().equals("true")){
                        add_restaurant_review.setEnabled(true);
                        add_restaurant_review.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReferenceUser.addValueEventListener(valueEventListener);
        get_restaurant_data();
    }

    private void get_restaurant_data(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RestaurantObject restaurant = dataSnapshot.getValue(RestaurantObject.class);

                if(restaurant!=null){
                    if(!restaurant.getImg().equals("")){
                        Picasso.get().load(restaurant.getImg()).into(restaurant_image);
                    }
                    if(critic.equals("true")){
                        add_restaurant_review.setEnabled(true);
                    }
                    restaurant_name.setText(restaurant.getR_name());
                    restaurant_specific.setText("Specific: " +restaurant.getR_type());
                    restaurant_address.setText("Address: "+restaurant.getR_address());
                    restaurant_description.setText(restaurant.getR_about());
                    restaurant_location.setText("Location: " +restaurant.getR_location());
                    restaurant_reviews_number.setText("(" + restaurant.getRating_number()+")");

                    float rate;
                    if(restaurant.getRating_number()>0){
                        rate = restaurant.getTotal_rating()/ restaurant.getRating_number();
                    }
                    else {rate =0;}
                    restaurant_rating.setRating(rate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabaseReferenceRestaurant.addValueEventListener(listener);
    }


}