package com.ffbf.forfoodiesbyfoodies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class SignUp extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    private ImageView profile_image;
    private EditText first_name, last_name, email, password;
    private Button sign_up, back_to_sign_in;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference, mDatabaseReferenceUser;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //hide the top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("images_of_users");
        mDatabaseReference = mDatabase.getReference().child("users");

        profile_image = findViewById(R.id.sign_up_add_image);
        first_name = findViewById(R.id.sign_up_first_name);
        last_name = findViewById(R.id.sign_up_last_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        back_to_sign_in = findViewById(R.id.back_to_sign_in);
        sign_up = findViewById(R.id.register_button);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_n = first_name.getText().toString().trim();
                String last_n = last_name.getText().toString().trim();
                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();

                //check if the fields are empty
                if (!TextUtils.isEmpty(first_n) && !TextUtils.isEmpty(last_n)
                        && !TextUtils.isEmpty(user_email) && !TextUtils.isEmpty(user_password)) {

                    sign_up_user(first_n, last_n, user_email, user_password);
                } else {
                    Toast.makeText(SignUp.this, "Make sure all boxes are completed", Toast.LENGTH_LONG).show();
                }
            }
        });

        back_to_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });

        //by clicking on the add image the user can select a photo from the phone
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
    }
    //register user in firebase
    private void sign_up_user(final String first_n, final String last_n, final String user_email, String user_password){
        mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    mDatabaseReferenceUser = mDatabase.getReference().child("users").child(id);
                    User user = new User(first_n, last_n, id, user_email, "false");
                    //upload user data in database
                    mDatabaseReference.child(id).setValue(user);
                    upload_photo();
                    Toast.makeText(SignUp.this, "Sign up has been successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                } else {
                    Toast.makeText(SignUp.this, "Email is already registered", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the user selected an image display it on the imageview
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            profile_image.setImageURI(mImageUri);
        }
    }
    // filepath for default profile image
    public String default_photo_filepath(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();

    }
    //image name
    public static String get_url(final String url) {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    //upload the profile photo of the user
    private void upload_photo() {
        //if no image has been selected a default profile image is assigned to the user
        if(mImageUri==null ){
            String url = default_photo_filepath(R.drawable.default_profile_photo);
            mImageUri = Uri.parse(url);
        }

        if (mImageUri != null) {
            List<String> pathSegments = mImageUri.getPathSegments();
            String lastSegment = pathSegments.get(pathSegments.size() - 1);
            final StorageReference filepath = mStorage.child(get_url(lastSegment));
            //upload the image
            //when the upload is successful get the url to the image and put it in the user object
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDatabaseReferenceUser.child("profile_image").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}