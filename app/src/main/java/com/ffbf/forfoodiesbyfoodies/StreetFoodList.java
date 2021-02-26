package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StreetFoodList extends AppCompatActivity {


    private RecyclerView recyclerView;
    private StreetFoodRecycler streetFoodRecycler;
    private List<StreetFoodObject> streetFoodList = new ArrayList<>();;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_food_list);
        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("street_food");
        mDatabaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.street_food_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference.addListenerForSingleValueEvent(listener);
    }
    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot dss: dataSnapshot.getChildren()){
                StreetFoodObject streetFood = dss.getValue(StreetFoodObject.class);
                streetFoodList.add(streetFood);
            }
            streetFoodRecycler = new StreetFoodRecycler(StreetFoodList.this, streetFoodList);
            recyclerView.setAdapter(streetFoodRecycler);

            streetFoodRecycler.notifyDataSetChanged();

            streetFoodRecycler.setOnItemClickListener(new StreetFoodRecycler.ViewHolder.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(StreetFoodList.this, DetailsStreetFood.class);
                    intent.putExtra("street_food", streetFoodList.get(position));
                    startActivity(intent);
                }
            });
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
}