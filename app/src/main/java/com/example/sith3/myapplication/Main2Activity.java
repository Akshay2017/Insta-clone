package com.example.sith3.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent log = new Intent(Main2Activity.this , MainActivity.class);

                startActivity(log);

            }
        });
        loadUsers();
    }


    private FirebaseRecyclerAdapter<Model , ListAdapter> mAdapter;

    private void loadUsers() {

        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
        Query userQuery = user.child("user");

        mAdapter = new FirebaseRecyclerAdapter<Model, ListAdapter>(Model.class , R.layout.listchat , ListAdapter.class , userQuery) {
            @Override
            protected void populateViewHolder(final ListAdapter viewHolder,final Model model,final int position) {

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Main2Activity.this, "working", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(Main2Activity.this , MessageActivity.class);
                        a.putExtra(MessageActivity.EXTRA , model);


                        startActivity(a);

                    }
                });

                viewHolder.bind(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        };
        list.setAdapter(mAdapter);

    }






/* @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("user");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model climate = dataSnapshot.getValue(Model.class);


                name.setText(climate.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/






}
