package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsList extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private RecyclerView recyclerView;
    private List<RestaurantObject> all_restaurants = new ArrayList<>();
    private RestaurantRecycler restaurantRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);
        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("restaurants");
        mDatabaseReference.keepSynced(true);
        mDatabaseReference.addListenerForSingleValueEvent(listener);

        recyclerView = findViewById(R.id.all_restaurants_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot dss : dataSnapshot.getChildren()) {
                RestaurantObject restaurant = dss.getValue(RestaurantObject.class);
                all_restaurants.add(restaurant);
            }
            restaurantRecycler = new RestaurantRecycler(RestaurantsList.this, all_restaurants);
            recyclerView.setAdapter(restaurantRecycler);
            restaurantRecycler.notifyDataSetChanged();
            restaurantRecycler.setOnItemClickListener(new RestaurantRecycler.ViewHolder.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(RestaurantsList.this, DetailsRestaurant.class);
                    intent.putExtra("Restaurant", all_restaurants.get(position));
                    startActivity(intent);
                }
            });
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
}