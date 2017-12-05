package com.example.sith3.chatclone.Me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sith3.chatclone.Post.Comments;
import com.example.sith3.chatclone.Models.Post;
import com.example.sith3.chatclone.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MeTimeline extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseDatabase mF;
    private FirebaseAuth mAuth;
    private RecyclerView postlist;
    private DatabaseReference mUserInfo;
    private DatabaseReference mLike;
    private Post ps;
    private boolean mProcessLike=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_timeline);

        mAuth = FirebaseAuth.getInstance();

        postlist = (RecyclerView) findViewById(R.id.mypost);
        postlist.setLayoutManager(new LinearLayoutManager(this));


        loadpost();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MeTimeline.this , Me.class);
        finish();
        startActivity(i);
    }

    private FirebaseRecyclerAdapter<Post, MeTimeline.MyPostviewholder> mAdapter;

    private void loadpost() {

        String currentuid=mAuth.getCurrentUser().getUid();
        mData=FirebaseDatabase.getInstance().getReference().child("post");
        Query query=mData.orderByChild("useruid").equalTo(currentuid);

        mAdapter=new FirebaseRecyclerAdapter<Post, MeTimeline.MyPostviewholder>(Post.class,R.layout.item_post,MeTimeline.MyPostviewholder.class,query) {
            @Override
            protected void populateViewHolder(final MeTimeline.MyPostviewholder viewHolder, final Post ps, int position) {

                mUserInfo=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
                final String postkey=getRef(position).getKey();
                mUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            String username=dataSnapshot.child("name").getValue().toString();
                            viewHolder.name.setText(username);

                            String userpic=dataSnapshot.child("imageurl").getValue().toString();


                            viewHolder.caption.setText(ps.getCaption());
                            Picasso.with(getApplicationContext()).load(ps.getImageurl()).into(viewHolder.postimage);
                            Picasso.with(getApplicationContext()).load(userpic).into(viewHolder.circleImageViewuserpic);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        viewHolder.like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProcessLike=true;

                                mLike=FirebaseDatabase.getInstance().getReference().child("like");
                                mLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {



                                            if (mProcessLike) {
                                                if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())) {

                                                    mLike.child(postkey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                    mProcessLike = false;

                                                } else {

                                                    mLike.child(postkey).child(mAuth.getCurrentUser().getUid()).setValue("Randomvalue");
                                                    mProcessLike = false;

                                                }


                                            }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        });

                    }
                });

                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent c=new Intent(MeTimeline.this,Comments.class);
                        c.putExtra(Comments.EXTRA,ps);
                        startActivity(c);

                    }
                });

                viewHolder.setLike(postkey);

            }


        };
        postlist.setAdapter(mAdapter);

    }
    public static class MyPostviewholder extends RecyclerView.ViewHolder {
        CircleImageView circleImageViewuserpic;
        TextView name,caption,nolikes;
        ImageView postimage;
        ImageView like,comment;
        private DatabaseReference mLike;
        private FirebaseAuth mAuth;
        public MyPostviewholder(View itemView) {
            super(itemView);
            mAuth = FirebaseAuth.getInstance();
            mLike=FirebaseDatabase.getInstance().getReference().child("like");
            circleImageViewuserpic= (CircleImageView) itemView.findViewById(R.id.userpic);
            name= (TextView) itemView.findViewById(R.id.username);
            caption= (TextView) itemView.findViewById(R.id.caption);
            postimage= (ImageView) itemView.findViewById(R.id.imagepost);
            like= (ImageView) itemView.findViewById(R.id.likeheart);
            nolikes= (TextView) itemView.findViewById(R.id.nolikes);
            comment= (ImageView) itemView.findViewById(R.id.comment);

        }

        public void setLike(final String postkey){

            mLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(postkey).getChildrenCount()!=0){

                        long totallikes=dataSnapshot.child(postkey).getChildrenCount();
                        String likes=Long.toString(totallikes);
                        like.setImageResource( R.drawable.ic_thumb_up_blue_24dp );
                        nolikes.setText(likes+"Likes");

                    }else {
                        like.setImageResource( R.drawable.ic_thumb_up_grey600_24dp );
                        nolikes.setText("");
                    }


                    if (mAuth.getCurrentUser()!=null){

                        if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())){

                            long totallikes=dataSnapshot.child(postkey).getChildrenCount();
                            String likes=Long.toString(totallikes);
                            like.setImageResource( R.drawable.ic_thumb_up_blue_24dp );
                            nolikes.setText(likes+"Likes");


                        }else {

                            if (dataSnapshot.child(postkey).getChildrenCount()!=0){

                                long totallikes=dataSnapshot.child(postkey).getChildrenCount();
                                String likes=Long.toString(totallikes);
                                like.setImageResource( R.drawable.ic_thumb_up_grey600_24dp );
                                nolikes.setText(likes+"Likes");


                            }
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
    }



}
