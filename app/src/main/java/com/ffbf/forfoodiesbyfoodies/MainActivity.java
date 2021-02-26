package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button sign_in, sign_up;
    private EditText email, password;
    private String user_id = "", admin = "false";
    private FirebaseAuth authenticate;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mProgressDialog = new ProgressDialog(this);
        //instantiate the firebase
        authenticate = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        sign_in = findViewById(R.id.sign_in_button);
        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);
        sign_up = findViewById(R.id.sign_up_button);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verify if the email or password fields are empty
                if(!TextUtils.isEmpty(email.getText().toString()) &&
                        !TextUtils.isEmpty(password.getText().toString())){
                    //call the user_sign_in method to authenticate the user
                    user_sign_in(email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this,"Email or password fields empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        //redirect the user to the sign up activity
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the sign up activity
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });
    }

    private void user_sign_in(String email, String password){
        //authenticate the user and get a confirmation
        authenticate.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //show message to the user
                    mProgressDialog.setMessage("Signing in.Please Wait");
                    mProgressDialog.show();
                    //get the unique user id
                    user_id = authenticate.getCurrentUser().getUid();
                    //check if the authenticated used is admin
                    is_user_admin(user_id);
                }else {
                    Toast.makeText(MainActivity.this,"Incorrect password or email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void is_user_admin(String user_id){
        //get the data from firebase that corresponds to the users that has authenticated
        mDatabaseReference = mDatabase.getReference().child("users").child(user_id);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user.isAdmin()) admin = "true";

                Intent intent = new Intent(MainActivity.this, MainMenu.class);

                intent.putExtra("admin", admin);
                startActivity(intent);
                mProgressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabaseReference.addListenerForSingleValueEvent(listener);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        user_id = "";
        admin = "false";


    }
}