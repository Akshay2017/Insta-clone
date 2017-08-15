package com.example.sith3.chatclone.Friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.sith3.chatclone.BottomNavigationView.BottomNavigationViewHelper;
import com.example.sith3.chatclone.Models.FriendsModel;
import com.example.sith3.chatclone.User_List.UserList;
import com.example.sith3.chatclone.Messages.MessageActivity;
import com.example.sith3.chatclone.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

public class Friend_List extends AppCompatActivity {
    private ImageView addchat;
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = Friend_List.this;
    private FriendsModel fmodel;
    private FirebaseAuth mAuth;
    private RecyclerView friendlist;
    private DatabaseReference mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addchat= (ImageView) findViewById(R.id.addchat);
        addchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addchat=new Intent(Friend_List.this,UserList.class);
                startActivity(addchat);
            }
        });


        mAuth=FirebaseAuth.getInstance();

        friendlist = (RecyclerView) findViewById(R.id.chatlist);
        friendlist.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNavigationView();

        load();
    }

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private FirebaseRecyclerAdapter<FriendsModel , FriendsAdapter> mAdapter;

    private void load() {
        DatabaseReference friends = FirebaseDatabase.getInstance().getReference().child("friends").child(mAuth.getCurrentUser().getUid());


        mAdapter = new FirebaseRecyclerAdapter<FriendsModel, FriendsAdapter>(FriendsModel.class, R.layout.item_friend_list, FriendsAdapter.class, friends) {
            @Override
            protected void populateViewHolder(final FriendsAdapter viewHolder, final FriendsModel fmodel, final int position) {
                mUserInfo=FirebaseDatabase.getInstance().getReference().child("user").child(fmodel.getUid());
                mUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String userpic=dataSnapshot.child("imageurl").getValue().toString();

                        Picasso.with(getApplicationContext()).load(userpic).into(viewHolder.circleImageViewuserpic);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(Friend_List.this, MessageActivity.class);
                        a.putExtra(MessageActivity.EXTRA, fmodel);
                        startActivity(a);

                    }
                });

                viewHolder.bind(fmodel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        friendlist.setAdapter(mAdapter);
    }
}
