package com.harshit.sharma.receptacle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class tracktheRequest extends AppCompatActivity
{

    TextView username;
    TextView requestId;
    TextView email;
    TextView area;
    TextView status;
    ImageView imageView;

    DatabaseReference mRef;
    FirebaseAuth mAuth;
    ChildEventListener childEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_the_request);

        username = (TextView) findViewById(R.id.tusername);
        requestId = (TextView) findViewById(R.id.trequestId);
        email= (TextView) findViewById(R.id.temail);
        area = (TextView) findViewById(R.id.tarea);
        status = (TextView) findViewById(R.id.tstatus);
        imageView = (ImageView) findViewById(R.id.img);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("requests");

        if(mAuth.getCurrentUser()!=null)
        {
            childEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName)
                {
                    Log.d("Child Added", "onChildAdded:" + dataSnapshot.getKey());
                    packet p = dataSnapshot.getValue(packet.class);
                    username.setText("User : "+mAuth.getCurrentUser().getDisplayName());
                    requestId.setText("Request id : "+p.requestId);
                    email.setText(" Email : " +p.userEmail);
                    area.setText("Area : "+p.area);
                    status.setText("Status : "+p.status);

                    byte[] decodedByteArray = Base64.decode(p.picture, Base64.NO_WRAP);
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedByteArray, 0,decodedByteArray.length));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mRef.addChildEventListener(childEventListener);


        }
        else
        {
            startActivity(new Intent(tracktheRequest.this,loginActivity.class));
            finish();
        }

    }
}
