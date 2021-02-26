package com.ffbf.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewOtherUser extends AppCompatActivity {

    private ImageView user_photo;
    private Button promote_user;
    private Button demote_user;
    private TextView critic;
    private TextView user_first_name;
    private TextView user_last_name;
    private TextView user_email;
    private TextView votes;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private User user;
    private String id;
    private String is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_user);
        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        user =  intent.getParcelableExtra("user");
        is_admin = intent.getStringExtra("is_admin");

        user_photo = findViewById(R.id.view_other_user_image);
        promote_user = findViewById(R.id.view_other_user_promote_to_critic);
        demote_user = findViewById(R.id.view_other_user_demote_user);
        critic = findViewById(R.id.view_other_user_is_food_critic);
        user_first_name = findViewById(R.id.view_other_user_first_name);
        user_last_name = findViewById(R.id.view_other_user_last_name);
        user_email = findViewById(R.id.view_other_user_email);
        votes = findViewById(R.id.view_other_user_votes_received);
        critic.setText("");

        show_data();

        mDatabaseReference = mDatabase.getReference().child("users").child(user.getUser_id());
        if(is_admin.equals("admin")){
            promote_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabaseReference.child("critic").setValue("true");
                    critic.setText("Food Critic");
                    promote_user.setEnabled(false);
                    demote_user.setEnabled(true);
                }
            });
            demote_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabaseReference.child("critic").setValue("false");
                    critic.setText("");
                    promote_user.setEnabled(true);
                    demote_user.setEnabled(false);
                }
            });
        } else {
            promote_user.setVisibility(View.GONE);
            demote_user.setVisibility(View.GONE);
        }
    }
    private void show_data(){
        if(!user.getProfile_image().equals("")) {
            Picasso.get().load(user.getProfile_image()).fit().into(user_photo);
        }
        if (user.getCritic().equals("true")) {
            critic.setText("Food critic");
            promote_user.setEnabled(false);
        }
        if(user.getCritic().equals("false")){
            demote_user.setEnabled(false);
        }
        user_first_name.setText(user.getFirst_name());
        user_last_name.setText(user.getLast_name());
        user_email.setText(user.getEmail());
        votes.setText(""+user.getVotes());
    }
}