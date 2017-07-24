package com.example.sith3.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MessageActivity extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseDatabase mF;
   private FirebaseAuth mAuth;
    private RecyclerView list;



    public static String EXTRA = "user";

     private TextView name, uid;
     private Model model;
    private Button send;
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();
        model = bundle.getParcelable(EXTRA);
        mAuth = FirebaseAuth.getInstance();



       uid = (TextView) findViewById(R.id.textView2);
        uid.setText(model.getUid());

//        measagelist = (RecyclerView) findViewById(R.id.mlist);
//        measagelist.setLayoutManager(new LinearLayoutManager(this));

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));


     text = (EditText) findViewById(R.id.editText4);

        name = (TextView) findViewById(R.id.name);
        name.setText(model.getName());

        send= (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessagetodatabase();
            }
        });
        loadmessage();
    }

     private void sendmessagetodatabase() {
        final String senderuid=mAuth.getCurrentUser().getUid();
        final String reciverid=model.getUid().toString();
        String mess =text.getText().toString();
        mData = FirebaseDatabase.getInstance().getReference().child("message").child(senderuid).child(reciverid).push();
        mData.child("senderuid").setValue(senderuid);
        mData.child("reciveruid").setValue(reciverid);
        mData.child("meassage").setValue(mess);


         mData = FirebaseDatabase.getInstance().getReference().child("message").child(reciverid).child(senderuid).push();
         mData.child("reciveruid").setValue(reciverid);
         mData.child("senderuid").setValue(senderuid);
         mData.child("meassage").setValue(mess);

         Toast.makeText(this, "enter data", Toast.LENGTH_SHORT).show();




    }

    private FirebaseRecyclerAdapter<Messages,MeassageAdapter> mAdapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MessageActivity.this , Main2Activity.class);
        finish();
        startActivity(i);
    }


    private void loadmessage(){

        final String senderuid=mAuth.getCurrentUser().getUid();
        final String reciverid=model.getUid().toString();

        final DatabaseReference message=FirebaseDatabase.getInstance().getReference();
        Query query=message.child("message").child(senderuid).child(reciverid);

        mAdapter=new FirebaseRecyclerAdapter<Messages, MeassageAdapter>(Messages.class, R.layout.meassgetext,MeassageAdapter.class,query) {
            @Override
            protected void populateViewHolder(final MeassageAdapter viewHolder, final Messages ms, int position) {



                viewHolder.bind(ms, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        };

    list.setAdapter(mAdapter);






    }
}
