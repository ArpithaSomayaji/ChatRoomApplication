package com.arpithasomayaji.chatroomapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public class FirebaseAuthService {

    private FirebaseAuth mAuth;

    public FirebaseAuth Initialize(){
        return mAuth = FirebaseAuth.getInstance();


    }

    public FirebaseAuth getAuth()
    {
        return mAuth;
    }

}
