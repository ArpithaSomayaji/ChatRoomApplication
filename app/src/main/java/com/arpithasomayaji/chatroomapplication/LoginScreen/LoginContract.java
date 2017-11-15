package com.arpithasomayaji.chatroomapplication.LoginScreen;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public interface LoginContract {

    interface UserActions{

        boolean checkNullsForFields( String email, String password);

        void doLogin( String email, String password, FirebaseAuth firebaseAuthService);

    }



    interface ViewActions{


        void dismissDialog();




        String getEmail();
        void markErrorForEmail();


        String getPassword();
        void markErrorForPassword();



        void showToastMessage(String message);
        void navigateAfterLogin();




    }

}
