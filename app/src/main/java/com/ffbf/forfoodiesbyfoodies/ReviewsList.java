package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReviewsList extends AppCompatActivity {

    private User user;
    private String currentUserID;
    private String restaurant_name;
    private ImageView vote;
    private int number_of_votes;
    private TextView email;
    private RecyclerView recyclerView;
    private ReviewRecycler reviewRecycler;
    private List<ReviewObject> reviewObjectList;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private com.google.firebase.database.DatabaseReference userDatabaseReference;
    private DatabaseReference votes_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);
        getSupportActionBar().hide();

        email = findViewById(R.id.review_row_user_email);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        vote = findViewById(R.id.review_row_like);

        currentUserID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();

        final Intent intent = getIntent();
        if (intent.getStringExtra("type").equals("Restaurant")) {
            restaurant_name = intent.getStringExtra("name");
            mDatabaseReference = mDatabase.getReference().child("restaurant_reviews").child(restaurant_name);
        } else {
            restaurant_name = intent.getStringExtra("name");
            mDatabaseReference = mDatabase.getReference().child("street_food_reviews").child(restaurant_name);
        }
        mDatabaseReference.keepSynced(true);

        reviewObjectList  = new ArrayList<>();

        recyclerView = findViewById(R.id.reviews_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference.addListenerForSingleValueEvent(listener);

    }


    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dss : dataSnapshot.getChildren()) {
                ReviewObject review = dss.getValue(ReviewObject.class);
                reviewObjectList.add(review);
            }
            reviewRecycler = new ReviewRecycler(ReviewsList.this, reviewObjectList);
            recyclerView.setAdapter(reviewRecycler);
            reviewRecycler.notifyDataSetChanged();
            reviewRecycler.setOnButtonClickListeners(new ReviewRecycler.OnButtonClickListeners() {
                @Override
                public void onEmailClick(int position) {
                    userDatabaseReference = mDatabase.getReference().child("users").child(reviewObjectList.get(position).getUserId());
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);

                            Intent intent = new Intent(ReviewsList.this, ViewOtherUser.class);
                            intent.putExtra("user", user);
                            intent.putExtra("is_admin", "user");
                            startActivity(intent);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    userDatabaseReference.addValueEventListener(valueEventListener);
                }

                @Override
                public void onVoteButtonClick(final int position) {
                    votes_reference = mDatabaseReference.child(reviewObjectList.get(position).getUserId()).child("votes");
                    final List<String> votes_list = new ArrayList<>();
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                String user_that_voted = ds.getKey();
                                votes_list.add(user_that_voted);
                            }
                            String thankingUserId = mUser.getUid();
                            Boolean user_has_voted = false;
                            for(String s:votes_list){
                                if(s.equals(thankingUserId)){
                                    Toast.makeText(ReviewsList.this, "You already voted", Toast.LENGTH_LONG).show();
                                    user_has_voted = true;
                                }
                            }
                            if(!user_has_voted){
                                votes_reference.child(thankingUserId).setValue("in");
                                userDatabaseReference = mDatabase.getReference().child("users").child(reviewObjectList.get(position).getUserId());
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user = dataSnapshot.getValue(User.class);
                                        number_of_votes = user.getVotes();
                                        userDatabaseReference.child("votes").setValue(++number_of_votes);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                };
                                userDatabaseReference.addListenerForSingleValueEvent(valueEventListener);
                                Toast.makeText(ReviewsList.this, "Your vote has been submitted", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    votes_reference.addListenerForSingleValueEvent(valueEventListener);
                }
            });
        }
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
}