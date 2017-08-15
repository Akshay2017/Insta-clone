package com.example.sith3.chatclone.Friend_Request;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sith3.chatclone.BottomNavigationView.BottomNavigationViewHelper;
import com.example.sith3.chatclone.Models.FriendRequest;
import com.example.sith3.chatclone.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptRequest extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = AcceptRequest.this;
    private DatabaseReference mData;
    private DatabaseReference mRef1;
    private DatabaseReference mFR;
    private FirebaseAuth mAuth;
    private RecyclerView list;
    private DatabaseReference mUserInfo;
    private DatabaseReference mUserI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_request);


        list = (RecyclerView) findViewById(R.id.acceptlist);
        list.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();


        mData=FirebaseDatabase.getInstance().getReference().child("friend_request");
        mUserI=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
        mFR=FirebaseDatabase.getInstance().getReference().child("friend_request").child(mAuth.getCurrentUser().getUid());

        setupBottomNavigationView();

        loadUsers();
    }

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private FirebaseRecyclerAdapter<FriendRequest, RequestAdapter> mAdapter;

    private void loadUsers() {

        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
        final Query userQuery = user.child("friend_request").child(mAuth.getCurrentUser().getUid());

        mAdapter=new FirebaseRecyclerAdapter<FriendRequest, RequestAdapter>(FriendRequest.class,R.layout.item_accept_request,RequestAdapter.class,userQuery) {
            @Override
            protected void populateViewHolder(final RequestAdapter viewHolder, final FriendRequest frmodel, final int position) {


                final String key=getRef(position).getKey();


                mFR.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(key)){
                            String req_type = dataSnapshot.child(key).child("request_type").getValue().toString();
                            if(req_type.equals("reciver")){

                                mUserInfo=FirebaseDatabase.getInstance().getReference().child("user").child(key);

                                mUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String username=dataSnapshot.child("name").getValue().toString();
                                        viewHolder.un.setText(username);

                                        String userpic=dataSnapshot.child("imageurl").getValue().toString();
                                        Picasso.with(getApplicationContext()).load(userpic).into(viewHolder.requestuserpic);


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }else {

                                viewHolder.un.setText("no friend request");
                                viewHolder.acceptr.setVisibility(View.INVISIBLE);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                    mFR = FirebaseDatabase.getInstance().getReference().child("friend_request").child(key);
                    mFR.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                String req_type = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("request_type").getValue().toString();
                                if (req_type.equals("send")) {
                                    viewHolder.acceptr.setText("Accept ");

                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                viewHolder.acceptr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String uname=dataSnapshot.child("name").getValue().toString();
                                String uemail=dataSnapshot.child("email").getValue().toString();
                                String upic=dataSnapshot.child("imageurl").getValue().toString();

                                mRef1 = FirebaseDatabase.getInstance().getReference().child("friends").child(mAuth.getCurrentUser().getUid()).child(key);
                                mRef1.child("name").setValue(uname);
                                mRef1.child("email").setValue(uemail);
                                mRef1.child("imageurl").setValue(upic);
                                mRef1.child("uid").setValue(key);




                                mUserI.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshots) {
                                        String cusername=dataSnapshots.child("name").getValue().toString();
                                        String cuseremail=dataSnapshots.child("email").getValue().toString();
                                        String cuserpic=dataSnapshots.child("imageurl").getValue().toString();

                                        mRef1 = FirebaseDatabase.getInstance().getReference().child("friends").child(key).child(mAuth.getCurrentUser().getUid());
                                        mRef1.child("name").setValue(cusername);
                                        mRef1.child("email").setValue(cuseremail);
                                        mRef1.child("imageurl").setValue(cuserpic);
                                        mRef1.child("uid").setValue(mAuth.getCurrentUser().getUid());


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mData.child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mData.child(key).child(mAuth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                viewHolder.acceptr.setText("unfriend");

                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AcceptRequest.this, key, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        };
        list.setAdapter(mAdapter);


    }

    public static class RequestAdapter extends RecyclerView.ViewHolder {

        CircleImageView requestuserpic;
        TextView un;
        Button acceptr;
        public RequestAdapter(View itemView) {
            super(itemView);
            requestuserpic= (CircleImageView) itemView.findViewById(R.id.requestuserpic);
            un = (TextView) itemView.findViewById(R.id.un);
            acceptr= (Button) itemView.findViewById(R.id.acceptr);
        }

    }
}
