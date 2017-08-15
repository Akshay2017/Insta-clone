package com.example.sith3.chatclone.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sith3.chatclone.Friends.Friend_List;
import com.example.sith3.chatclone.Models.FriendsModel;
import com.example.sith3.chatclone.Models.Messages;
import com.example.sith3.chatclone.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MessageActivity extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseDatabase mF;
   private FirebaseAuth mAuth;
    private RecyclerView measagelist;



    public static String EXTRA = "user";

     private TextView name;
     private FriendsModel fmodel;
    private Button send;
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();
        fmodel = bundle.getParcelable(EXTRA);
        mAuth = FirebaseAuth.getInstance();


       measagelist = (RecyclerView) findViewById(R.id.list);
        measagelist.setLayoutManager(new LinearLayoutManager(this));




     text = (EditText) findViewById(R.id.editText4);

        name = (TextView) findViewById(R.id.name);
        name.setText(fmodel.getName());

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
        final String reciverid=fmodel.getUid().toString();
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

         text.setText("");



    }

    private FirebaseRecyclerAdapter<Messages,RecyclerView.ViewHolder> mAdapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MessageActivity.this , Friend_List.class);
        finish();
        startActivity(i);
    }


    private void loadmessage(){



        final String senderuid=mAuth.getCurrentUser().getUid();
        final String reciverid=fmodel.getUid().toString();

        final DatabaseReference message=FirebaseDatabase.getInstance().getReference();
        Query query=message.child("message").child(senderuid).child(reciverid);

        mAdapter=new FirebaseRecyclerAdapter<Messages, RecyclerView.ViewHolder>(Messages.class, R.layout.meassgetext1,RecyclerView.ViewHolder.class,query) {

            private final int OUTGOING=1;
            private final int INCOMING=2;


            @Override
            protected void populateViewHolder(final RecyclerView.ViewHolder viewHolder, final Messages ms, int position) {
                if (currentuid(ms)){
                  populateviewholdeeroutgoing((Outgoing) viewHolder,ms);
                }else {
                    populateviewholdeerin((Incoming) viewHolder,ms);
                }



            }



            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                switch (viewType){

                    case OUTGOING:

                        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.meassgetext1,parent,false);
                        return new Outgoing(view);

                    case INCOMING:

                        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.meassgetext2,parent,false);
                        return new Incoming(view);

                }
                return super.onCreateViewHolder(parent,viewType);
            }

            @Override
            public int getItemViewType(int position) {
              super.getItemViewType(position);

                Messages ms=getItem(position);
                if (currentuid(ms)){

                    return OUTGOING;
                }
                return INCOMING;
            }

            private boolean currentuid(Messages ms) {
                String senderuid=mAuth.getCurrentUser().getUid();
                if (senderuid.equalsIgnoreCase(ms.getSenderuid())){

                    return true;
                }
                return false;
            }

            private void populateviewholdeeroutgoing(Outgoing outgoing, Messages ms){

                outgoing.bind(ms, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }




            private void populateviewholdeerin(Incoming incoming,Messages ms){

                incoming.bind(ms, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            class Outgoing extends RecyclerView.ViewHolder{

                TextView out;

                public Outgoing(View itemView) {
                    super(itemView);
                    out= (TextView) itemView.findViewById(R.id.data);

                }

                public void bind(Messages ms, View.OnClickListener clickListener){

                    out.setText(ms.getMeassage());

                }
            }

            class Incoming extends RecyclerView.ViewHolder{

                TextView in;

                public Incoming(View itemView) {
                    super(itemView);
                    in= (TextView) itemView.findViewById(R.id.reciver);

                }

                public void bind(Messages ms, View.OnClickListener clickListener){


                    in.setText(ms.getMeassage());

                }
            }

        };

        measagelist.setAdapter(mAdapter);






    }


}
