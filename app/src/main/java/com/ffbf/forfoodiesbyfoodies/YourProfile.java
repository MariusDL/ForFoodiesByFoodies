package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YourProfile extends AppCompatActivity {



    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;
    private String id;
    private boolean has_image = false;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView profile_image;
    private Button edit_password, edit_profile, apply_changes, change_image;
    private EditText first_name, last_name, email;
    private TextView votes, critic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your profile");
        setContentView(R.layout.activity_your_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference("images_of_users");
        mDatabase = FirebaseDatabase.getInstance();
        id = mAuth.getCurrentUser().getUid();
        mDatabaseReference = mDatabase.getReference().child("users").child(id);

        profile_image = findViewById(R.id.your_profile_image);
        change_image = findViewById(R.id.your_profile_change_image);
        edit_profile = findViewById(R.id.your_profile_edit_profile);
        apply_changes = findViewById(R.id.your_profile_apply_changes);
        edit_password = findViewById(R.id.your_profile_edit_password);
        first_name = findViewById(R.id.your_profile_first_name);
        last_name = findViewById(R.id.your_profile_last_name);
        email = findViewById(R.id.your_profile_email);
        votes = findViewById(R.id.your_profile_votes);
        critic = findViewById(R.id.your_profile_critic);

        change_image.setEnabled(false);
        apply_changes.setEnabled(false);
        first_name.setEnabled(false);
        last_name.setEnabled(false);
        email.setEnabled(false);

        get_user_from_database();

        //make the boxes editable so the user can change profile details
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_changes.setEnabled(true);
                first_name.setEnabled(true);
                last_name.setEnabled(true);
                email.setEnabled(true);
                edit_profile.setEnabled(false);
                change_image.setEnabled(true);

            }
        });

        //update the details in the database
        apply_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String f_name = first_name.getText().toString();
                final String l_name = last_name.getText().toString();
                final String user_email = email.getText().toString();
                if (!TextUtils.isEmpty(f_name) || !TextUtils.isEmpty(l_name) || !TextUtils.isEmpty(user_email)){
                    mDatabaseReference.child("first_name").setValue(f_name);
                    mDatabaseReference.child("last_name").setValue(l_name);
                    mDatabaseReference.child("email").setValue(user_email);
                    mUser.updateEmail(user_email);
                    if(has_image){
                        save_img_in_database();
                    }
                    change_image.setEnabled(false);
                    apply_changes.setEnabled(false);
                    first_name.setEnabled(false);
                    last_name.setEnabled(false);
                    email.setEnabled(false);
                    edit_profile.setEnabled(true);
                    Toast.makeText(YourProfile.this, "Profile updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_user_password();
            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                has_image = true;

            }
        });
    }

    private void get_user_from_database(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user!= null){
                    first_name.setText(user.getFirst_name());
                    last_name.setText(user.getLast_name());
                    email.setText(user.getEmail());
                    votes.setText(""+user.getVotes());
                    if (user.getCritic().equals("true")) {
                        critic.setText("Food critic");
                    } else {
                        critic.setText("");
                    }
                    if (!user.getProfile_image().equals("")) {
                        Picasso.get().load(user.getProfile_image()).into(profile_image);
                    }
                } else {
                    Toast.makeText(YourProfile.this, "Database error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabaseReference.addValueEventListener(valueEventListener);
    }

    private void save_img_in_database() {
        if (mImageUri != null) {
            List<String> pathSegments = mImageUri.getPathSegments();
            String lastSegment = pathSegments.get(pathSegments.size() - 1);
            final StorageReference filepath = mStorage.child(get_url(lastSegment));
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDatabaseReference.child("profile_image").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }

    public static String get_url(final String url){
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData();
            profile_image.setImageURI(mImageUri);
        }
    }

    private void change_user_password(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter new password:");
        final EditText new_password = new EditText(this);
        alert.setView(new_password);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n_pass = new_password.getText().toString();
                mUser.updatePassword(n_pass);
                Toast.makeText(YourProfile.this, "Password has been updated", Toast.LENGTH_LONG).show();
                return;
            }
        });
        alert.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        alert.show();
    }
}