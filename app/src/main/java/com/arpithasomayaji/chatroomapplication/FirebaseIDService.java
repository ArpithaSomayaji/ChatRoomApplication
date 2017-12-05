package com.arpithasomayaji.chatroomapplication;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Arpitha.Somayaji on 12/4/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken(); //get refreshed token
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid(); //get currentto get uid
        if(user!=null){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();// create a reference to userUid in database
            if(refreshedToken!=null) //
                mDatabase.child("Users").child(user).child("device_token").setValue(refreshedToken);
             //creates a new node of user's token and set its value to true.
            else
                Log.i(TAG, "onTokenRefresh: token was null");
        }
        Log.d(TAG, "Refreshed token SEND TO FIREBASE: " + refreshedToken);
    }

}
