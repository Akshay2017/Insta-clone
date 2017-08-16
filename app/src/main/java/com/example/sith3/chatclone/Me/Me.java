package com.example.sith3.chatclone.Me;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sith3.chatclone.BottomNavigationView.BottomNavigationViewHelper;
import com.example.sith3.chatclone.Login.Login;
import com.example.sith3.chatclone.R;
import com.example.sith3.chatclone.Registration.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Me extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabse;
    private TextView changeprofile,timeline,mname,logout;
    private CircleImageView mpic;
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = Me.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());

        changeprofile= (TextView) findViewById(R.id.changemyprofile);
        timeline= (TextView) findViewById(R.id.mytimeline);

        mname= (TextView) findViewById(R.id.myname);

        logout= (TextView) findViewById(R.id.logout);

        mpic= (CircleImageView) findViewById(R.id.mypic);

        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Me.this,Userprofile.class);
                startActivity(i);
            }
        });

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Me.this,MeTimeline.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent log = new Intent(Me.this , Login.class);

                startActivity(log);
            }
        });

        mUserDatabse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String myname=dataSnapshot.child("name").getValue().toString();

                String userpic=dataSnapshot.child("imageurl").getValue().toString();

                mname.setText(myname);

                Picasso.with(getApplicationContext()).load(userpic).into(mpic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setupBottomNavigationView();
    }
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
