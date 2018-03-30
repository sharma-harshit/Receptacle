package com.harshit.sharma.receptacle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class thankyou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        Thread t =new Thread(){
            public void run()
            {
                try
                {
                    sleep(4000);
                    Intent i9=new Intent(thankyou.this,user.class);
                    startActivity(i9);
                    finish();
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
}
