package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {

    private Menu top_menu;
    private Intent intent;
    private boolean admin = false;
    private String id;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private Button restaurant_button, street_food_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Main menu");
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("users").child(id);
        mDatabaseReference.keepSynced(true);

        restaurant_button = findViewById(R.id.view_restaurants);
        street_food_button = findViewById(R.id.view_street_food);

        intent = getIntent();
        //get the value of isAdmin from the intent
        String admin_user = intent.getStringExtra("admin");

        if(admin_user.equals("true")){
            admin = true;
        }
        //if street food button is pressed
        street_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, StreetFoodList.class));
            }
        });
        //if restaurants button is pressed
        restaurant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, RestaurantsList.class));
            }
        });
        mDatabaseReference.addListenerForSingleValueEvent(check);
    }
    ValueEventListener check = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            if(user.isAdmin()){
                admin = true;
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
    //show the top menu depending on the type of user(admin or normal user)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.top_menu = menu;

        if(!admin){
            menu.clear();
            getMenuInflater().inflate(R.menu.top_menu_user, menu);
        } else {
            menu.clear();
            getMenuInflater().inflate(R.menu.top_menu_admin, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.view_profile){
            startActivity(new Intent(MainMenu.this, YourProfile.class));
        }
        if(item.getItemId()==R.id.all_users){
            startActivity(new Intent(MainMenu.this, UserList.class));
        }
        if(item.getItemId()==R.id.add_restaurant){
            Intent new_intent = new Intent(MainMenu.this, AddLocation.class);
            new_intent.putExtra("location_type", "Restaurant");
            startActivity(new_intent);
        }
        if(item.getItemId()==R.id.add_street_food){
            Intent new_intent = new Intent(MainMenu.this, AddLocation.class);
            new_intent.putExtra("location_type", "StreetFood");
            startActivity(new_intent);
        }
        if(item.getItemId() == R.id.sign_out_option){
            mAuth.signOut();
            startActivity(new Intent(MainMenu.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDatabaseReference.removeEventListener(check);
        mAuth.signOut();
        finish();
    }
}