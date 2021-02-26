package com.ffbf.forfoodiesbyfoodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class AddLocation extends AppCompatActivity {
    private Button add_location;
    private EditText location_name;
    private EditText location_specific;
    private EditText location_location;
    private EditText location_address;
    private EditText location_description;
    private ImageView location_image;
    private Uri mImageUri;
    private String imageUrl;
    private boolean has_image = false;
    private String location_type;
    private String id;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        location_image = findViewById(R.id.add_location_image);
        add_location = findViewById(R.id.add_new_location_button);
        location_name = findViewById(R.id.add_location_name);
        location_specific = findViewById(R.id.add_location_specific);
        location_location = findViewById(R.id.add_location_location);
        location_address = findViewById(R.id.add_location_address);
        location_description = findViewById(R.id.add_location_description);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        Intent i = getIntent();
        location_type = i.getStringExtra("location_type");

        if(location_type.equals("Restaurant")) {
            mDatabaseReference = mDatabase.getReference().child("restaurants");
            mStorage = FirebaseStorage.getInstance().getReference("restaurant_images");
            getSupportActionBar().setTitle("Add new restaurant");
        } else {
            mDatabaseReference = mDatabase.getReference().child("street_food");
            mStorage = FirebaseStorage.getInstance().getReference("street_food_images");
            getSupportActionBar().setTitle("Add new street food");
        }

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l_name = location_name.getText().toString();
                String l_specific = location_specific.getText().toString();
                String l_address = location_address.getText().toString();
                String l_description = location_description.getText().toString();
                String l_location = location_location.getText().toString();

                if(!TextUtils.isEmpty(l_name) && (!TextUtils.isEmpty(l_specific) && !TextUtils.isEmpty(l_address) && !TextUtils.isEmpty(l_description)
                        && !TextUtils.isEmpty(l_location)&& has_image)){
                    add_new_location(l_name, l_specific, l_address, l_description, l_location);
                }

            }
        });
        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
    }
    private void add_new_location(final String l_name, final String l_specific, final String l_address, final String l_description, final String l_location){

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(l_name).exists()){
                    if(location_type.equals("Restaurant")) {
                        RestaurantObject restaurant = new RestaurantObject(l_name, l_specific, l_description, l_address, l_location);
                        mDatabaseReference.child(l_name).setValue(restaurant);
                    } else {
                        // or create a street food object
                        StreetFoodObject street_food = new StreetFoodObject(l_name,l_description,l_address,l_location,l_specific,id);
                        mDatabaseReference.child(l_name).setValue(street_food);
                    }
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
                                        imageUrl = uri.toString();
                                        mDatabaseReference.child(l_name).child("img").setValue(uri.toString());
                                    }
                                });
                            }
                        });
                    }
                    finish();
                } else {
                    Toast.makeText(AddLocation.this,"Location is already in the system", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public static String get_url(final String url){
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData();
            location_image.setImageURI(mImageUri);
            has_image = true;
        }
    }
}