package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String id;
    private DatabaseReference mDatabaseReference;
    private RecyclerView recyclerView;
    private UserRecycler userRecycler;
    private List<User> userList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        id = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("users");
        mDatabaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.all_users_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference.addListenerForSingleValueEvent(listener);
    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dss: dataSnapshot.getChildren()){
                User user = dss.getValue(User.class);
                userList.add(user);
            }
            userRecycler = new UserRecycler(UserList.this, userList);
            recyclerView.setAdapter(userRecycler);
            userRecycler.notifyDataSetChanged();
            userRecycler.setOnItemClickListener(new UserRecycler.ViewHolder.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(UserList.this, ViewOtherUser.class);
                    intent.putExtra("user", userList.get(position));
                    intent.putExtra("is_admin", "admin");
                    startActivity(intent);
                }
            });
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
}