package com.example.sith3.chatclone.User_List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sith3.chatclone.Friend_Request.AcceptRequest;
import com.example.sith3.chatclone.Friends.Friend_List;
import com.example.sith3.chatclone.Login.Login;
import com.example.sith3.chatclone.Messages.MessageActivity;
import com.example.sith3.chatclone.Models.Users;
import com.example.sith3.chatclone.Others.Request;
import com.example.sith3.chatclone.Post.PostAdapter;
import com.example.sith3.chatclone.R;
import com.example.sith3.chatclone.Registration.Registration;
import com.example.sith3.chatclone.TimeLine.TimeLine;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class UserList extends AppCompatActivity {



    private DatabaseReference mData;
    private DatabaseReference f;
    private FirebaseDatabase mF;
    private FirebaseAuth mAuth;
    private RecyclerView list;
    private Button upost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();



        loadUsers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(UserList.this , Friend_List.class);
        finish();
        startActivity(i);
    }

    private FirebaseRecyclerAdapter<Users, ListAdapter> mAdapter;

    private void loadUsers() {

        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
        Query userQuery = user.child("user");

        mAdapter = new FirebaseRecyclerAdapter<Users, ListAdapter>(Users.class , R.layout.item_request_list, ListAdapter.class , userQuery) {
            @Override
            protected void populateViewHolder(final ListAdapter viewHolder, final Users users, final int position) {


                Picasso.with(getApplicationContext()).load(users.getImageurl()).into(viewHolder.circleImageView);

                viewHolder.request.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        // state no friend
                        mData=FirebaseDatabase.getInstance().getReference().child("friend_request");
                        mData.child(mAuth.getCurrentUser().getUid()).child(users.getUid()).child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mData.child(users.getUid()).child(mAuth.getCurrentUser().getUid()).child("request_type").setValue("reciver").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                viewHolder.request.setText("cancel");

                                            }
                                        });


                                    }else {

                                        Toast.makeText(UserList.this, "request cannot send suceesfully", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        // cancel request state

                        String status= (String) viewHolder.request.getText();

                        if (status.equals("cancel")){

                            mData.child(mAuth.getCurrentUser().getUid()).child(users.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mData.child(users.getUid()).child(mAuth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            viewHolder.request.setText("request");
                                        }
                                    });
                                }
                            });



                        }


                        }

                });


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        final String uid= users.getUid();
//                        Toast.makeText(UserList.this, uid, Toast.LENGTH_SHORT).show();
//
//                        Intent a = new Intent(UserList.this , Request.class);
//                        a.putExtra(MessageActivity.EXTRA , users);
//                        startActivity(a);



                    }
                });

                viewHolder.bind(users, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }
        };
        list.setAdapter(mAdapter);

    }

}
