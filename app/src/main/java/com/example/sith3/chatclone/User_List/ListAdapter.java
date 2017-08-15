package com.example.sith3.chatclone.User_List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sith3.chatclone.Models.Users;
import com.example.sith3.chatclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sith3 on 7/19/2017.
 */

public class ListAdapter extends RecyclerView.ViewHolder{

    CircleImageView circleImageView;
    TextView item_list;
    Button request;
    private FirebaseAuth mAuth;
    private DatabaseReference mFR;
    private DatabaseReference mRef1;
    private DatabaseReference mData;

    public ListAdapter(View itemView) {
        super(itemView);
        mAuth = FirebaseAuth.getInstance();
        mFR=FirebaseDatabase.getInstance().getReference().child("friend_request").child(mAuth.getCurrentUser().getUid());
        mRef1 = FirebaseDatabase.getInstance().getReference().child("friends");
        circleImageView= (CircleImageView) itemView.findViewById(R.id.profilepic);
        item_list = (TextView) itemView.findViewById(R.id.list);
        request= (Button) itemView.findViewById(R.id.fr);
        mData=FirebaseDatabase.getInstance().getReference().child("friend_request");

    }
    public void bind(final Users users, View.OnClickListener clickListener){
      item_list.setText(users.getName());



        mFR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid= users.getUid();
                if (dataSnapshot.hasChild(uid)){
                    String req_type = dataSnapshot.child(uid).child("request_type").getValue().toString();
                    if(req_type.equals("send")){
                        request.setText("cancel ");

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


                    request.setText("unfriend");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
