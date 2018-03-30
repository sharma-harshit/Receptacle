package com.harshit.sharma.receptacle;

/*
 Created by HARSHIT on 3/24/2018.
*/

import android.util.Base64;

import java.util.List;

public class packet
{
    static int id;
    int requestId;
    String userEmail;
    String area;
    String picture;
    String status;
    static
    {
        id=10009;
    }
    packet() {
        requestId = id;
        id++;
        userEmail = "";
        area = "";
    }

    packet(String userEmail, String area, byte[] picture)
    {
        requestId = id;
        id++;
        this.userEmail = userEmail;
        this.area = area;
        this.picture= Base64.encodeToString(picture, Base64.DEFAULT);
        this.status="To be reviewed.";
    }
}

