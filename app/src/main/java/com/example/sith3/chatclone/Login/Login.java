package com.example.sith3.chatclone.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sith3.chatclone.TimeLine.TimeLine;
import com.example.sith3.chatclone.R;
import com.example.sith3.chatclone.Registration.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private Button login;
    private EditText email,password;
    private TextView reg;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        mref= FirebaseDatabase.getInstance().getReference().child("user");

        email= (EditText) findViewById(R.id.loginemail);
        password= (EditText) findViewById(R.id.loginpassword);
        reg= (TextView) findViewById(R.id.register);
        login= (Button) findViewById(R.id.log);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(Login.this,Registration.class);
                startActivity(r);
            }
        });

    }

    private void log() {

        String emails=email.getText().toString().trim();
        String passwords=password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(emails , passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent i=new Intent(Login.this,TimeLine.class);
                    finish();
                    startActivity(i);

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

        Intent i = new Intent(Login.this , TimeLine.class);
        finish();
        startActivity(i);

    }

}
