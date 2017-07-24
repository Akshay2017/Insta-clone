package com.example.sith3.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageActivity extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseDatabase mF;
   private FirebaseAuth mAuth;


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
    }

     private void sendmessagetodatabase() {
        final String senderuid=mAuth.getCurrentUser().getUid();
        final String reciverid=model.getUid().toString();
        String mess =text.getText().toString();
        mData = FirebaseDatabase.getInstance().getReference().child("message").child(senderuid).child(reciverid);
        mData.child("senderuid").setValue(senderuid);
        mData.child("reciveruid").setValue(reciverid);
        mData.child("meassage").setValue(mess);

         Toast.makeText(this, "enter data", Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MessageActivity.this , Main2Activity.class);
        finish();
        startActivity(i);
    }
}
