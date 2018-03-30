package com.harshit.sharma.receptacle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import android.Manifest;
import com.google.firebase.database.FirebaseDatabase;


import java.io.ByteArrayOutputStream;


public class user extends AppCompatActivity
{
    private DatabaseReference mRef;
    TextView username;
    Button logoutButton,track,feedback,capture,request;
    EditText areaobj;
    ImageView  imagePreview;
    private FirebaseAuth mFirebaseAuth;
    Activity activity;
    byte[] picByteArray;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        activity = this;
        logoutButton = (Button) findViewById(R.id.logout);
        track = (Button) findViewById(R.id.track_request);
        capture = (Button) findViewById(R.id.open_camera);
        request = (Button) findViewById(R.id.request);
        imagePreview = (ImageView) findViewById(R.id.image_preview);
        username = (TextView) findViewById(R.id.username);
        areaobj = (EditText) findViewById(R.id.area);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("requests");
        if (mFirebaseAuth.getCurrentUser() != null)
        {
            username.setText(mFirebaseAuth.getCurrentUser().getDisplayName().toString());


            logoutButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AuthUI.getInstance()
                            .signOut(user.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Intent loginIntent = new Intent(activity, splashscreen.class);
                                        startActivity(loginIntent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(user.this, "Failed to Sign Out", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
            });


            capture.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                Manifest.permission.CAMERA))
                        {

                        }
                        else
                            {
                            String[] a = {Manifest.permission.CAMERA};
                            ActivityCompat.requestPermissions(activity, a, 100);
                        }
                    }
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                        {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                }
            });

        }
       else
        {
            startActivity(new Intent(activity,loginActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePreview.setImageBitmap(imageBitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            picByteArray = byteArrayOutputStream .toByteArray();
        }
    }
    boolean validate()
    {
        if(picByteArray!=null&& (!areaobj.getText().equals(""))&&mFirebaseAuth.getCurrentUser()!=null )
        {
            return true;
        }
        Toast.makeText(activity, "First fill all the details", Toast.LENGTH_SHORT).show();
        return false;
    }
    public void trackonClick(View view)
    {
            startActivity(new Intent(user.this,tracktheRequest.class));
    }
    public void requestonClick(View view)
    {
        if(validate())
        {
            packet data;
            String userEmail =mFirebaseAuth.getCurrentUser().getEmail().toString();
            String area=areaobj.getText().toString();
            data = new packet(userEmail,areaobj.getText().toString(),picByteArray);

            DatabaseReference reference = mRef.child(mFirebaseAuth.getCurrentUser().getDisplayName());
            reference.setValue(data);
            Toast.makeText(activity, "Request Accepted",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(activity, thankyou.class));

        }
    }
}