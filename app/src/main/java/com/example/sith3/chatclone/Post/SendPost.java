package com.example.sith3.chatclone.Post;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sith3.chatclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Akshay on 8/1/2017.
 */

public class SendPost extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2 ;


    private Button upload_post;
    private ImageView post_image;
    private EditText status;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabse;
    private DatabaseReference mUserInfo;
    private StorageReference mStorageRef;

    Uri imageHoldUri = null;


    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_post);
        mProgress = new ProgressDialog(this);
        status= (EditText) findViewById(R.id.status);

        mAuth = FirebaseAuth.getInstance();
        mUserInfo = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mUserDatabse= FirebaseDatabase.getInstance().getReference().child("post").push();

        post_image= (ImageView) findViewById(R.id.postimage);

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //LOGIC FOR PROFILE PICTURE
                profilePicSelection();

            }
        });


        upload_post= (Button) findViewById(R.id.uploadpost);

        upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });


    }



    private void profilePicSelection() {
        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SendPost.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        //CHOOSE CAMERA

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                post_image.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    private void post() {

        final String  userStatus = status.getText().toString().trim();
        final String uid=mAuth.getCurrentUser().getUid();

        if( imageHoldUri != null )
        {
            mProgress.setTitle("Saveing Profile");
            mProgress.setMessage("Please wait....");
            mProgress.show();

            StorageReference mChildStorage = mStorageRef.child("Posts").child(imageHoldUri.getLastPathSegment());
            String profilePicUrl = imageHoldUri.getLastPathSegment();

            mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri imageUrl = taskSnapshot.getDownloadUrl();


                    String key=mUserDatabse.getRef().getKey();

                    mUserDatabse.child("caption").setValue(userStatus.toString());
                    mUserDatabse.child("imageurl").setValue(imageUrl.toString());
                    mUserDatabse.child("useruid").setValue(uid.toString());
                    mUserDatabse.child("postkey").setValue(key.toString());




                    mProgress.dismiss();
                    finish();


                }
            });
        }else
        {

            Toast.makeText(SendPost.this, "Please select the profile pic", Toast.LENGTH_LONG).show();

        }




    }



}
