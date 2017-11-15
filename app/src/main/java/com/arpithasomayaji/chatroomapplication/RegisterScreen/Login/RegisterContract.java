package com.arpithasomayaji.chatroomapplication.RegisterScreen.Login;

import android.app.ProgressDialog;

import com.arpithasomayaji.chatroomapplication.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public interface RegisterContract {
    interface UserActions{

         boolean checkNullsForFields(String name, String email, String s);

         void registerNewUser(String name, String email, String password, FirebaseAuth firebaseAuthService);

    }


    interface ViewActions{


        void dismissDialog();


        String getName();
        void markErrorName();


        String getEmail();
        void markErrorForEmail();


        String getPassword();
        void markErrorForPassword();


        String getConfirmPassword();
        void markErrorForConfirmPassword();

        void showToastMessage(String message);
        void navigateAfterRegister();




    }


}
