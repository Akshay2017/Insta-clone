package com.example.sith3.chatclone.Others;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sith3.chatclone.Me.Userprofile;
import com.example.sith3.chatclone.Models.Users;
import com.example.sith3.chatclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Request extends AppCompatActivity {
    private DatabaseReference mData;
    DatabaseReference f;
    private FirebaseDatabase mF;
    private FirebaseAuth mAuth;
    public static String EXTRA = "user";
    private TextView name;
    private Users users;
    private Button send ,userprofile;

    String mcurrent_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mcurrent_state="no_friend";

        Bundle bundle = getIntent().getExtras();
        users = bundle.getParcelable(EXTRA);
        mAuth = FirebaseAuth.getInstance();
        name = (TextView) findViewById(R.id.name);
        name.setText(users.getName());

        mData=FirebaseDatabase.getInstance().getReference().child("friend_request");

        final DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference().child("friends");

        final DatabaseReference userinfo = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());



        mData.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(users.getUid())){
                    String req_type=dataSnapshot.child(users.getUid()).child("request_type").getValue().toString();
                    if (req_type.equals("reciver")){
                        mcurrent_state="req_recived";
                        send.setText("Accept");
                    }else if(req_type.equals("send")){
                        mcurrent_state="req_send";
                        send.setText("cancel ");

                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRef1.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(users.getUid())){

                    mcurrent_state="friends";
                    send.setText("unfriends");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userprofile= (Button) findViewById(R.id.changeprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p=new Intent(Request.this,Userprofile.class);
                startActivity(p);
            }
        });


       send= (Button) findViewById(R.id.request);
        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
           @Override
            public void onClick(View v) {


                // state no friend
//                if (mcurrent_state.equals("no_friend")){
//                    send.setEnabled(false);
//
//                    mData.child(mAuth.getCurrentUser().getUid()).child(users.getUid()).child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                mData.child(users.getUid()).child(mAuth.getCurrentUser().getUid()).child("request_type").setValue("reciver").addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//
//                                        mcurrent_state="req_send";
//                                        send.setText("cancel friend request");
//
//
//
//                                        Toast.makeText(Request.this,"request send suceesfully", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//                            }else {
//
//                                Toast.makeText(Request.this, "request cannot send suceesfully", Toast.LENGTH_SHORT).show();
//
//                            }
//                            send.setEnabled(true);
//                        }
//                    });
//
//
//
//                }


                // cancel request state

//                if (mcurrent_state.equals("req_send")){
//
//                    mData.child(mAuth.getCurrentUser().getUid()).child(users.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            mData.child(users.getUid()).child(mAuth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    send.setEnabled(true);
//                                    mcurrent_state="not_friend";
//                                    send.setText("send friend request");
//
//                                }
//                            });
//                        }
//                    });
//
//
//
//                }

                // accept request


                if (mcurrent_state.equals("req_recived")){

                    userinfo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override

                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Users m=dataSnapshot.getValue(Users.class);
                            String username=m.getName();
                            String uid=m.getUid();
                            String email=m.getEmail();

                            String modelUid= users.getUid();
                            String modelEmail= users.getEmail();
                            String modelName= users.getName();


                            f = FirebaseDatabase.getInstance().getReference().child("friends").child(uid).child(modelUid);

                            f.child("email").setValue(modelEmail.toString());
                            f.child("name").setValue(modelName.toString());
                            f.child("uid").setValue(modelUid.toString());


                            f = FirebaseDatabase.getInstance().getReference().child("friends").child(modelUid).child(uid);


                            f.child("email").setValue(email.toString());
                            f.child("uid").setValue(uid.toString());
                            f.child("name").setValue(username.toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });







                    mData.child(mAuth.getCurrentUser().getUid()).child(users.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mData.child(users.getUid()).child(mAuth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    send.setEnabled(true);
                                    mcurrent_state="friends";
                                    send.setText("unfriend");

                                }
                            });
                        }
                    });




                }

            }
        });
    }



}
