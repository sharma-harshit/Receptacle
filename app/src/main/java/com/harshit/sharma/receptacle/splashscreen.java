package com.harshit.sharma.receptacle;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class splashscreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread t =new Thread(){
            public void run()
            {
                try
                {
                    //Toast.makeText(splashscreen.this, "Welcome",Toast.LENGTH_LONG).show();
                    sleep(5000);
                    Intent i1=new Intent(splashscreen.this,loginActivity.class);
                    startActivity(i1);
                    finish();
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
}
