package com.example.sith3.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity  {

    private Button sign , singup;
    private EditText name,email,password;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mref=FirebaseDatabase.getInstance().getReference().child("user");
        name= (EditText) findViewById(R.id.editText2);
        email= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText3);
        sign= (Button) findViewById(R.id.button);
        singup= (Button) findViewById(R.id.button3);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


    }

    private void signin() {


        String emails=email.getText().toString().trim();
        String passwords=password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(emails , passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent i=new Intent(MainActivity.this,Main2Activity.class);
                    finish();
                    startActivity(i);

                }
            }
        });
    }

    private void signup() {

        final String names=name.getText().toString().trim();
        final String emails=email.getText().toString().trim();
        String passwords=password.getText().toString().trim();


       mAuth.createUserWithEmailAndPassword(emails , passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {

               if(task.isSuccessful()){
                   final String uid=mAuth.getCurrentUser().getUid();
                   mref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                   mref.child("name").setValue(names);
                   mref.child("email").setValue(emails);
                   mref.child("uid").setValue(uid);



                   Intent i=new Intent(MainActivity.this,Main2Activity.class);
                   startActivity(i);
                   finish();
               }
           }
       });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){

            success(mAuth.getCurrentUser());

        }
    }

    private void success(FirebaseUser currentUser) {

        Intent i = new Intent(MainActivity.this , Main2Activity.class);
        finish();
        startActivity(i);

    }
}
