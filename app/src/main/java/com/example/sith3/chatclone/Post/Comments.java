package com.example.sith3.chatclone.Post;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sith3.chatclone.Models.Comment;
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

public class Comments extends AppCompatActivity {
    private DatabaseReference mData;
    private DatabaseReference mUserInfo;
    private DatabaseReference mComments;
    private FirebaseDatabase mF;
    private FirebaseAuth mAuth;
    private RecyclerView commentlist;
    private EditText text;
    private Post ps;
    public static String EXTRA = "post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Bundle bundle = getIntent().getExtras();
        ps = bundle.getParcelable(EXTRA);
        mAuth = FirebaseAuth.getInstance();


        commentlist = (RecyclerView) findViewById(R.id.commentview);
        commentlist.setLayoutManager(new LinearLayoutManager(this));





        text = (EditText) findViewById(R.id.usercomments);

        FloatingActionButton fabchat = (FloatingActionButton) findViewById(R.id.commentsend);
        fabchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendcommentstodatabase();

            }
        });

       loadcomments();

    }


    private void sendcommentstodatabase() {
                final String postkey=ps.getPostkey().toString();
                final String senderuid=mAuth.getCurrentUser().getUid();
                String mess =text.getText().toString();
                mData = FirebaseDatabase.getInstance().getReference().child("comments").child(postkey).push();
                mData.child("uid").setValue(senderuid);
                mData.child("meassage").setValue(mess);


                text.setText("");

        Toast.makeText(this, "enter data", Toast.LENGTH_SHORT).show();

    }
    private FirebaseRecyclerAdapter<Comment, Commentviewholder> mAdapter;

    private void loadcomments() {
        Query query=FirebaseDatabase.getInstance().getReference().child("comments").child(ps.getPostkey());

        mAdapter=new FirebaseRecyclerAdapter<Comment, Commentviewholder>(Comment.class,R.layout.item_comments_list,Commentviewholder.class,query) {
            @Override
            protected void populateViewHolder(final Commentviewholder viewHolder, final Comment cm, final int position) {

                viewHolder.usercom.setText(cm.getMeassage());

                mUserInfo=FirebaseDatabase.getInstance().getReference().child("user").child(cm.getUid());
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
            }
        };
        commentlist.setAdapter(mAdapter);
    }

    public static class Commentviewholder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewuserpic;
        TextView usercom;

        public Commentviewholder(View itemView) {
            super(itemView);
            circleImageViewuserpic= (CircleImageView) itemView.findViewById(R.id.cpic);
            usercom= (TextView) itemView.findViewById(R.id.userm);
        }
    }
}
