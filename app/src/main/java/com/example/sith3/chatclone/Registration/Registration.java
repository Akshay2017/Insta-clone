package com.example.sith3.chatclone.Registration;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sith3.chatclone.TimeLine.TimeLine;
import com.example.sith3.chatclone.Login.Login;
import com.example.sith3.chatclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Registration extends AppCompatActivity  {

    private TextView singin;
    private AppCompatButton singup;
    private EditText name,email,password,repassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    private StorageReference mStorageRef;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2 ;
    ImageView userImageProfileView;

    Uri imageHoldUri = null;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mref=FirebaseDatabase.getInstance().getReference().child("user");
        name= (EditText) findViewById(R.id.editText2);
        email= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText3);
        repassword= (EditText) findViewById(R.id.input_reEnterPassword);
        singin= (TextView) findViewById(R.id.link_login);
        singup= (AppCompatButton) findViewById(R.id.button3);

        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();

            }
        });

        mProgress = new ProgressDialog(this);

        userImageProfileView = (ImageView) findViewById(R.id.userpic);


        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //LOGIC FOR PROFILE PICTURE
                profilePicSelection();

            }
        });



      /* sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });*/

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


    }

    private void profilePicSelection() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
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

                userImageProfileView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    private void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }


        final String names=name.getText().toString().trim();
        final String emails=email.getText().toString().trim();
        String passwords=password.getText().toString().trim();


       mAuth.createUserWithEmailAndPassword(emails , passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {

               if(task.isSuccessful()){
                   if( imageHoldUri != null )
                   {
                       mProgress.setTitle("Saveing Profile");
                       mProgress.setMessage("Please wait....");
                       mProgress.show();

                       StorageReference mChildStorage = mStorageRef.child("User_Profile").child(imageHoldUri.getLastPathSegment());
                       String profilePicUrl = imageHoldUri.getLastPathSegment();

                       mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                               final Uri imageUrl = taskSnapshot.getDownloadUrl();

                               final String uid=mAuth.getCurrentUser().getUid();
                               mref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                               mref.child("name").setValue(names);
                               mref.child("email").setValue(emails);
                               mref.child("uid").setValue(uid);
                               mref.child("imageurl").setValue(imageUrl.toString());
                               mProgress.dismiss();


                               Intent i=new Intent(Registration.this,Login.class);
                               startActivity(i);
                               finish();

                           }
                       });
                   }else
                   {

                       Toast.makeText(Registration.this, "Please select the profile pic", Toast.LENGTH_LONG).show();

                   }

               }
           }
       });
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        singup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String uname = name.getText().toString();
        String uemail = email.getText().toString();
        String upassword = password.getText().toString();
        String reEnterPassword = repassword.getText().toString();

        if (uname.isEmpty() || uname.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }


        if (uemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }


        if (upassword.isEmpty() || upassword.length() < 6 || upassword.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(upassword))) {
            repassword.setError("Password Do not match");
            valid = false;
        } else {
            repassword.setError(null);
        }

        return valid;
    }

}
